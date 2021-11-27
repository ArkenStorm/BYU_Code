#include "csapp.h"
#include "parser.h"

#include<errno.h>
#include<fcntl.h>
#include<stdlib.h>
#include<stdio.h>
#include<sys/epoll.h>
#include<sys/socket.h>
#include<string.h>

/* Recommended max cache and object sizes */
#define MAX_CACHE_SIZE 1049000
#define MAX_OBJECT_SIZE 102400
#define MAXEVENTS 64

/* You won't lose style points for including this long line in your code */
static const char *user_agent_hdr = "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:10.0.3) Gecko/20120305 Firefox/10.0.3\r\n";

struct client_info {
	int fd;
	char desc[100];
};

enum fd_state{READ_REQUEST, SEND_REQUEST, READ_RESPONSE, SEND_RESPONSE};
struct client_state {
	int client_fd;
	int server_fd;
	enum fd_state state;
	char buf[MAX_OBJECT_SIZE];
	int client_bytes_read;
	int server_write_bytes; // amount to write to server
	int server_bytes_written; // number of bytes written so far
	int server_bytes_read;
	int client_write_bytes; // amount to write to client
};
struct client_state client_states[500];
int num_states;
struct client_state get_state(struct client_state *states, int fd);
void get_request(struct client_state state);
int connect_to_webserver(char *hostname, char *port);

int main(int argc, char **argv) {
	int listenfd, connfd;
	socklen_t clientlen;
	struct sockaddr_storage clientaddr;
	int efd;
	struct epoll_event event;
	struct epoll_event *events;
	int i;

	struct client_info listen_event;
	struct client_info *new_client;
	struct client_info *active_event;

	size_t n;

	if (argc != 2) {
		fprintf(stderr, "usage: %s <port>\n", argv[0]);
		exit(0);
	}

	listenfd = Open_listenfd(argv[1]);

	if ((efd = epoll_create1(0)) < 0) {
		fprintf(stderr, "error creating epoll fd\n");
		exit(1);
	}

	listen_event.fd = listenfd;
	sprintf(listen_event.desc, "Listen file descriptor");
	if (fcntl(listenfd, F_SETFL, fcntl(listenfd, F_GETFL, 0) | O_NONBLOCK) < 0) {
		fprintf(stderr, "error setting socket option\n");
		exit(1);
	}

	event.data.ptr = &listen_event;
	event.events = EPOLLIN | EPOLLET;
	if (epoll_ctl(efd, EPOLL_CTL_ADD, listenfd, &event) < 0) {
		fprintf(stderr, "error adding event\n");
		exit(1);
	}

	/* Buffer where events are returned */
	events = calloc(MAXEVENTS, sizeof(event));

	while (1) {
		// wait for event to happen (no timeout)
		n = epoll_wait(efd, events, MAXEVENTS, 1000);

		if (n == 0) {
			continue;
		}
		else if (n < 0) {
			if (errno == EBADF || errno == EFAULT || errno == EINTR || errno == EINVAL) {
				printf("Error in epoll_wait, code: %d\n", errno);
				exit(1);
			}
		}

		for (i = 0; i < n; i++) {
			active_event = (struct client_info *)(events[i].data.ptr);

			printf("new activity from %s\n", active_event->desc);
			if ((events[i].events & EPOLLERR) ||
					(events[i].events & EPOLLHUP) ||
					(events[i].events & EPOLLRDHUP)) {
				/* An error has occured on this fd */
				fprintf (stderr, "epoll error on %s\n", active_event->desc);
				close(active_event->fd);
				free(active_event);
				continue;
			}

			if (listenfd == active_event->fd) { //line:conc:select:listenfdready
				clientlen = sizeof(struct sockaddr_storage); 
				while (1) {
					connfd = accept(listenfd, (struct sockaddr *)&clientaddr, &clientlen);
					if (connfd < 0) {
						break;
					}

					if (fcntl(connfd, F_SETFL, fcntl(connfd, F_GETFL, 0) | O_NONBLOCK) < 0) {
						fprintf(stderr, "error setting socket option\n");
						exit(1);
					}

					// add event to epoll file descriptor
					new_client = (struct client_info *)malloc(sizeof(struct client_info));
					new_client->fd = connfd;
					sprintf(new_client->desc, "client with file descriptor %d", connfd);

					event.data.ptr = new_client;
					event.events = EPOLLIN | EPOLLET;
					if (epoll_ctl(efd, EPOLL_CTL_ADD, connfd, &event) < 0) {
						fprintf(stderr, "error adding event\n");
						exit(1);
					}

					struct client_state new_state;
					new_state.client_fd = connfd;
					new_state.state = READ_REQUEST;
					new_state.client_bytes_read = 0;
					client_states[num_states++] = new_state;
				}
			} else { //line:conc:select:listenfdready
				while (1) {
					struct client_state state = get_state(client_states, active_event->fd);
					if (state.client_fd == -1 && state.server_fd == -1) break; // no state was found
					if (state.state == READ_REQUEST) {
						get_request(state);
						if (state.client_fd == -1) {
							if (epoll_ctl(efd, EPOLL_CTL_DEL, state.client_fd, &event) < 0) {
								fprintf(stderr, "error deleting event\n");
								exit(1);
							}
							if (epoll_ctl(efd, EPOLL_CTL_DEL, state.server_fd, &event) < 0) {
								fprintf(stderr, "error deleting event\n");
								exit(1);
							}
							break;
						}
						if (is_complete_request(state.buf)) {
							http_header headers[64];
							char request[MAX_OBJECT_SIZE];
							char method[MAX_OBJECT_SIZE];
							char hostname[MAX_OBJECT_SIZE];
							char port[MAX_OBJECT_SIZE];
							char uri[MAX_OBJECT_SIZE];
							char temp[MAX_OBJECT_SIZE];
							char webserver_request[MAX_OBJECT_SIZE];
							int num_headers = parse_request(request, method, hostname, port, uri, headers);
							num_headers = add_header("Host", hostname, headers, num_headers);
							num_headers = add_header("User-Agent", (char *)user_agent_hdr, headers, num_headers);
							num_headers = add_header("Connection", "close", headers, num_headers);
							num_headers = add_header("Proxy-Connection", "close", headers, num_headers);

							// create request
							sprintf(temp, "%s %s HTTP/1.0\r\n", method, uri);
							strcat(webserver_request, temp);
							for (int i = 0; i < num_headers; i++) {
								sprintf(temp, "%s: %s\r\n", headers[i].name, headers[i].value);
								strcat(webserver_request, temp);
							}
							strcat(webserver_request, "\r\n");

							state.server_fd = connect_to_webserver(hostname, port);
							
							new_client->fd = state.server_fd;
							sprintf(new_client->desc, "client with file descriptor: %d", state.server_fd);
							event.data.ptr = new_client;
							event.events = EPOLLOUT | EPOLLET;
							if (epoll_ctl(efd, EPOLL_CTL_ADD, state.server_fd, &event) < 0) {
								fprintf(stderr, "error adding event\n");
								exit(1);
							}
							state.server_write_bytes = strlen(webserver_request);
							strcpy(state.buf, webserver_request);
							state.server_bytes_written = 0;
							state.state = SEND_REQUEST;
						}
					}
					else if (state.state == SEND_REQUEST) {
						while (state.server_bytes_written < state.server_write_bytes) {
							int nwrite = state.server_bytes_written;
							nwrite = write(state.server_fd, state.buf + state.server_bytes_written, state.server_write_bytes - state.server_bytes_written);
							if (nwrite >= 0) state.server_bytes_written += nwrite;
							if (nwrite == 0) {

								new_client->fd = state.server_fd;
								sprintf(new_client->desc, "client with file descriptor: %d", state.server_fd);
								event.data.ptr = new_client;
								event.events = EPOLLIN | EPOLLET;
								if (epoll_ctl(efd, EPOLL_CTL_MOD, state.server_fd, &event) < 0) {
									fprintf(stderr, "error adding event\n");
									exit(1);
								}
								state.state = READ_RESPONSE;
							}
							else if (nwrite < 0) {
								if (errno == EAGAIN || errno == EWOULDBLOCK) {
									// this means there's more stuff to come
									break;
								}
								else {
									close(state.client_fd);
									close(state.server_fd);
									state.client_fd = -1;
									state.server_fd = -1; // error, ignore this forever
									if (epoll_ctl(efd, EPOLL_CTL_DEL, state.client_fd, &event) < 0) {
										fprintf(stderr, "error deleting event\n");
										exit(1);
									}
									if (epoll_ctl(efd, EPOLL_CTL_DEL, state.server_fd, &event) < 0) {
										fprintf(stderr, "error deleting event\n");
										exit(1);
									}
									break;
								}
							}
						}
					}
					else if (state.state == READ_RESPONSE) {
						int nread = 0;
						int server_bytes_read = 0;
						do {
							nread = read(state.server_fd, state.buf + server_bytes_read, MAX_OBJECT_SIZE);
							if (nread < 0) {
								if (errno == EAGAIN || errno == EWOULDBLOCK) {
									// this means there's more stuff to come
									break;
								}
								else {
									close(state.client_fd);
									close(state.server_fd);
									state.client_fd = -1;
									state.server_fd = -1; // error, ignore this forever
									if (epoll_ctl(efd, EPOLL_CTL_DEL, state.client_fd, &event) < 0) {
										fprintf(stderr, "error deleting event\n");
										exit(1);
									}
									if (epoll_ctl(efd, EPOLL_CTL_DEL, state.server_fd, &event) < 0) {
										fprintf(stderr, "error deleting event\n");
										exit(1);
									}
									break;
								}
							}
							server_bytes_read += nread;
						} while (nread != 0);
						if (nread == 0) {
							new_client->fd = state.client_fd;
							sprintf(new_client->desc, "client with file descriptor: %d", state.client_fd);
							event.data.ptr = new_client;
							event.events = EPOLLOUT | EPOLLET;
							if (epoll_ctl(efd, EPOLL_CTL_MOD, state.client_fd, &event) < 0) {
								fprintf(stderr, "error adding event\n");
								exit(1);
							}
							state.client_write_bytes = server_bytes_read;
							state.state = SEND_RESPONSE;
							close(state.server_fd);
						}
					}
					else if (state.state == SEND_RESPONSE) {
						int nwrite = 0;
						int client_bytes_written = 0;
						while ((nwrite = write(state.client_fd, state.buf + client_bytes_written, state.client_write_bytes - client_bytes_written)) != 0) {
							if (nwrite < 0) {
								if (errno == EAGAIN || errno == EWOULDBLOCK) {
									// this means there's more stuff to come
									break;
								}
								else {
									close(state.client_fd);
									close(state.server_fd);
									state.client_fd = -1;
									state.server_fd = -1; // error, ignore this forever
									if (epoll_ctl(efd, EPOLL_CTL_DEL, state.client_fd, &event) < 0) {
										fprintf(stderr, "error deleting event\n");
										exit(1);
									}
									if (epoll_ctl(efd, EPOLL_CTL_DEL, state.server_fd, &event) < 0) {
										fprintf(stderr, "error deleting event\n");
										exit(1);
									}
									break;
								}
							}
							client_bytes_written += nwrite;
						}
						if (nwrite == 0) {
							close(state.client_fd);
						}
					}
				}
			}
		}
	}
	free(events);
}

struct client_state get_state(struct client_state *states, int fd) {
	struct client_state fake_state;
	fake_state.client_fd = -1;
	fake_state.server_fd = -1;
	for (int i = 0; i < num_states; i++) {
		if (states[i].client_fd == fd || states[i].server_fd == fd) {
			return states[i];
		}
	}
	return fake_state;
}

void get_request(struct client_state state) {
	int nread = 0;
	while (1) {
		nread = recv(state.client_fd, state.buf + state.client_bytes_read, MAX_OBJECT_SIZE, 0);

		if (nread < 0) {
			if (errno == EAGAIN || errno == EWOULDBLOCK) {
				// this means there's more stuff to come
				return;
			}
			else {
				close(state.client_fd);
				close(state.server_fd);
				state.client_fd = -1;
				state.server_fd = -1; // error, ignore this forever
			}
		}
		if (nread == 0)
			break;
		state.client_bytes_read += nread;
		if (is_complete_request(state.buf)) {
			return;
		}
	}
	printf("Premature connection closure.\n");
	return;
}

int connect_to_webserver(char *hostname, char *port) {
	struct addrinfo hints;
	struct addrinfo *result, *rp;
	int s, sfd;

	memset(&hints, 0, sizeof(struct addrinfo));
	hints.ai_family = AF_UNSPEC;    /* Allow IPv4 or IPv6 */
	hints.ai_socktype = SOCK_STREAM; /* Datagram socket */
	hints.ai_flags = 0;
	hints.ai_protocol = 0;          /* Any protocol */

	s = getaddrinfo(hostname, port, &hints, &result);
	if (s != 0) {
		fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(s));
		exit(EXIT_FAILURE);
	}

	for (rp = result; rp != NULL; rp = rp->ai_next) {
		sfd = socket(rp->ai_family, rp->ai_socktype, rp->ai_protocol);
		if (sfd == -1)
			continue;

		if (connect(sfd, rp->ai_addr, rp->ai_addrlen) != -1)
			break;                  /* Success */

		close(sfd);
	}

	if (rp == NULL) {               /* No address succeeded */
		fprintf(stderr, "Could not connect.\n");
		exit(EXIT_FAILURE);
	}

	if (fcntl(sfd, F_SETFL, fcntl(sfd, F_GETFL, 0) | O_NONBLOCK) < 0) {
		fprintf(stderr, "Error registering socket option.\n");
		exit(1);
	}

	freeaddrinfo(result);

	return sfd;
}