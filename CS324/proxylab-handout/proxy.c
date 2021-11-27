#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>
#include "parser.h"
#include "sbuf.h"
#include "csapp.h"

/* Recommended max cache and object sizes */
#define MAX_CACHE_SIZE 1049000
#define MAX_OBJECT_SIZE 102400
#define MAXLINE 8192
#define NTHREADS  20
#define SBUFSIZE  20

void *thread(void *vargp);
int get_request(int sockfd, char *buffer);
sbuf_t sbuf;

/* You won't lose style points for including this long line in your code */
static const char *user_agent_hdr = "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:10.0.3) Gecko/20120305 Firefox/10.0.3\r\n";

int main(int argc, char **argv) {
    int i, listenfd, connfd;
	socklen_t clientlen;
	struct sockaddr_in ip4addr;
	struct sockaddr_storage clientaddr;
	pthread_t tid; 

	if (argc != 2) {
		fprintf(stderr, "usage: %s <port>\n", argv[0]);
		exit(0);
	}

	sbuf_init(&sbuf, SBUFSIZE); //line:conc:pre:initsbuf
	for (i = 0; i < NTHREADS; i++)  /* Create worker threads */ //line:conc:pre:begincreate
		pthread_create(&tid, NULL, thread, NULL);               //line:conc:pre:endcreate

	ip4addr.sin_family = AF_INET;
	ip4addr.sin_port = htons(atoi(argv[1]));
	ip4addr.sin_addr.s_addr = INADDR_ANY;
	if ((listenfd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
		perror("socket error");
		exit(EXIT_FAILURE);
	}
	if (bind(listenfd, (struct sockaddr*)&ip4addr, sizeof(struct sockaddr_in)) < 0) {
		close(listenfd);
		perror("bind error");
		exit(EXIT_FAILURE);
	}
	if (listen(listenfd, 100) < 0) {
		close(listenfd);
		perror("listen error");
		exit(EXIT_FAILURE);
	}

	while (1) {
		clientlen = sizeof(struct sockaddr_storage);
		connfd = accept(listenfd, (struct sockaddr *) &clientaddr, &clientlen);
		sbuf_insert(&sbuf, connfd); /* Insert connfd in buffer */
	}
    
    return 0;
}

void *thread(void *vargp) {
    char buffer[MAX_OBJECT_SIZE];
    char method[512];
    char hostname[512];
	char port[512];
	char uri[512];
	http_header headers[1024];
	int num_headers = 0;
    int result = 0;
    int webserver_fd = 0;
    char webserver_request[MAX_OBJECT_SIZE];
    char response[MAX_OBJECT_SIZE];

	pthread_detach(pthread_self()); 
	while (1) { 
		int connfd = sbuf_remove(&sbuf); /* Remove connfd from buffer */ //line:conc:pre:removeconnfd
        if ((result = get_request(connfd, buffer)) <= 0) {
            printf("Incomplete request.\n");
        }
        else {
            num_headers = parse_request(buffer, method, hostname, port, uri, headers);
            num_headers = add_header("Host", hostname, headers, num_headers);
            num_headers = add_header("User-Agent", (char *)user_agent_hdr, headers, num_headers);
            num_headers = add_header("Connection", "close", headers, num_headers);
            num_headers = add_header("Proxy-Connection", "close", headers, num_headers);

            webserver_fd = open_clientfd(hostname, port);

            sprintf(webserver_request, "%s %s HTTP/1.0\r\n", method, uri);
            for (size_t i = 0; i < num_headers; i++) {
                sprintf(webserver_request, "%s: %s\r\n", headers[i].name, headers[i].value);
            }
            sprintf(webserver_request, "\r\n");
            printf("wbs_req: %s\n", webserver_request);
            int wbs_req_bytes = strlen(webserver_request);

            // send to webserver
            int total_bytes_written = 0;
            while (total_bytes_written != wbs_req_bytes) {
		        total_bytes_written += write(webserver_fd, webserver_request + total_bytes_written, wbs_req_bytes - total_bytes_written);
            }

            int socket_bytes_read = 0;

            // read response from webserver
            int read_value = -1;
            while (read_value != 0) {
                read_value = read(webserver_fd, response + socket_bytes_read, MAX_OBJECT_SIZE);
                socket_bytes_read += read_value;
            }

            // send to initial client
            int written_bytes = 0;
            total_bytes_written = 0;
            while ((written_bytes = write(connfd, response + total_bytes_written, socket_bytes_read - total_bytes_written)) != 0) {
                total_bytes_written += written_bytes;
            }
        }

		close(connfd);
	}
}

int get_request(int sockfd, char *buffer) {
    int nread = 0;
    int total_bytes_read = 0;
    while (1) {
        nread = recv(sockfd, buffer, MAX_OBJECT_SIZE, 0);

        if (nread < 0) {
            printf("Short count in get_request.\n");
            return -1;               /* Ignore failed request */
        }
        if (nread == 0)
            break;
        total_bytes_read += nread;
        if (is_complete_request(buffer)) {
            return total_bytes_read;
        }
    }
    printf("Premature connection closure.\n");
    return -1;
}