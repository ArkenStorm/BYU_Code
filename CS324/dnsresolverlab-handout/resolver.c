#include <arpa/inet.h>
#include <netinet/in.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <time.h>
#include <netdb.h>
#include <unistd.h>
#include <netinet/ip.h>

#define BUF_SIZE 512

/**
 * =======================================
 * DString - Super simple dynamically resized string
 */

typedef struct {
	char *cstr;
	char *end; // For quick appends
	size_t cap; // Not strlen, capacity
} DString;

size_t dstr_len(DString *str) {
	return str->end - str->cstr;
}

void dstr_grow(DString *str, size_t new_cap) {
	if (new_cap <= str->cap) return;
	size_t offset = dstr_len(str);
	str->cstr = realloc(str->cstr, sizeof(char)*new_cap);
	str->end = str->cstr + offset;
	str->cap = new_cap;
}

void dstr_append_n(DString *str, const char *new_str, size_t new_len) {
	if (new_len == 0) return;
	size_t need_len = dstr_len(str) + new_len + 1;
	dstr_grow(str, need_len);

	memcpy(str->end, new_str, new_len);
	str->end += new_len;
	*str->end = 0; // Ensure null terminator
}

void dstr_append(DString *str, const char *new_str) {
	size_t new_len = strlen(new_str);
	dstr_append_n(str, new_str, new_len);
}

DString *dstr_new(size_t initial_capacity) {
	DString *str = malloc(sizeof(DString));
	str->cstr = NULL;
	str->end = NULL;
	str->cap = 0;

	dstr_grow(str, initial_capacity);
	*str->cstr = 0; // Initial NULL terminator
	return str;
}

void dstr_free(DString *str) {
	free(str->cstr);
	free(str);
}

// Reformat the string into an immutable cstr
char *dstr_finish(DString *str) {
	char *cstr = str->cstr;
	size_t final_size = dstr_len(str) + 1;
	free(str); // Erase the DString struct

	return realloc(cstr, final_size);
}

uint16_t read_2_bytes(unsigned char *wire, size_t *indexp) {
	uint16_t data = ntohs(*((uint16_t *)(wire + *indexp)));
	*indexp += 2;
	return data;
}

typedef unsigned int dns_rr_ttl;
typedef unsigned short dns_rr_type;
typedef unsigned short dns_rr_class;
typedef unsigned short dns_rdata_len;
typedef unsigned short dns_rr_count;
typedef unsigned short dns_query_id;
typedef unsigned short dns_flags;

typedef struct {
	char *name;
	dns_rr_type type;
	dns_rr_class class;
	dns_rr_ttl ttl;
	dns_rdata_len rdata_len;
	unsigned char *rdata;
} dns_rr;

struct dns_answer_entry;
struct dns_answer_entry {
	char *value;
	struct dns_answer_entry *next;
};
typedef struct dns_answer_entry dns_answer_entry;

void free_answer_entries(dns_answer_entry *ans) {
	dns_answer_entry *next;
	while (ans != NULL) {
		next = ans->next;
		free(ans->value);
		free(ans);
		ans = next;
	}
}

void print_bytes(unsigned char *bytes, int byteslen) {
	int i, j, byteslen_adjusted;
	unsigned char c;

	if (byteslen % 8) {
		byteslen_adjusted = ((byteslen / 8) + 1) * 8;
	} else {
		byteslen_adjusted = byteslen;
	}
	for (i = 0; i < byteslen_adjusted + 1; i++) {
		if (!(i % 8)) {
			if (i > 0) {
				for (j = i - 8; j < i; j++) {
					if (j >= byteslen_adjusted) {
						printf("  ");
					} else if (j >= byteslen) {
						printf("  ");
					} else if (bytes[j] >= '!' && bytes[j] <= '~') {
						printf(" %c", bytes[j]);
					} else {
						printf(" .");
					}
				}
			}
			if (i < byteslen_adjusted) {
				printf("\n%02X: ", i);
			}
		} else if (!(i % 4)) {
			printf(" ");
		}
		if (i >= byteslen_adjusted) {
			continue;
		} else if (i >= byteslen) {
			printf("   ");
		} else {
			printf("%02X ", bytes[i]);
		}
	}
	printf("\n");
}

void canonicalize_name(char *name) {
	/*
	 * Canonicalize name in place.  Change all upper-case characters to
	 * lower case and remove the trailing dot if there is any.  If the name
	 * passed is a single dot, "." (representing the root zone), then it
	 * should stay the same.
	 *
	 * INPUT:  name: the domain name that should be canonicalized in place
	 */
	
	int namelen, i;

	// leave the root zone alone
	if (strcmp(name, ".") == 0) {
		return;
	}

	namelen = strlen(name);
	// remove the trailing dot, if any
	if (name[namelen - 1] == '.') {
		name[namelen - 1] = '\0';
	}

	// make all upper-case letters lower case
	for (i = 0; i < namelen; i++) {
		if (name[i] >= 'A' && name[i] <= 'Z') {
			name[i] += 32;
		}
	}
}

int name_ascii_to_wire(char *name, unsigned char *wire) {
	/* 
	 * Convert a DNS name from string representation (dot-separated labels)
	 * to DNS wire format, using the provided byte array (wire).  Return
	 * the number of bytes used by the name in wire format.
	 *
	 * INPUT:  name: the string containing the domain name
	 * INPUT:  wire: a pointer to the array of bytes where the
	 *              wire-formatted name should be constructed
	 * OUTPUT: the length of the wire-formatted name.
	 */
	int offset = 0;
	name = strdup(name);
	char *token = strtok(name, ".");
	while (token != NULL) {
		int token_length = strlen(token);
		wire[offset] = (unsigned char)token_length;
		for (size_t i = 0; i < token_length; i++) {
			offset += 1;
			wire[offset] = token[i];
		}
		offset += 1;
		token = strtok(NULL, ".");
	}
	wire[offset] = '\0';
	free(name);
	return ++offset;
}

char *name_ascii_from_wire(unsigned char *wire, int *indexp) {
	/* 
	 * Extract the wire-formatted DNS name at the offset specified by
	 * *indexp in the array of bytes provided (wire) and return its string
	 * representation (dot-separated labels) in a char array allocated for
	 * that purpose.  Update the value pointed to by indexp to the next
	 * value beyond the name.
	 *
	 * INPUT:  wire: a pointer to an array of bytes
	 * INPUT:  indexp, a pointer to the index in the wire where the
	 *              wire-formatted name begins
	 * OUTPUT: a string containing the string representation of the name,
	 *              allocated on the heap.
	 */
	DString *name = dstr_new(20);
	int cursor = *indexp;
	int following_bytes = wire[cursor];
	while (following_bytes != 0) {
		if (following_bytes >= 0xc0) {
			cursor = wire[cursor + 1];
			following_bytes = wire[cursor];
		}
		for (size_t i = 0; i < following_bytes; i++) {
			cursor++;
			dstr_append_n(name, (unsigned char *)(wire + cursor), 1);
		}
		cursor++;
		dstr_append(name, ".");
		following_bytes = wire[cursor];
	}
	char *name_ascii = dstr_finish(name);
	canonicalize_name(name_ascii);
	return name_ascii;
}

dns_rr rr_from_wire(unsigned char *wire, int *indexp, int query_only) {
	/* 
	 * Extract the wire-formatted resource record at the offset specified by
	 * *indexp in the array of bytes provided (wire) and return a 
	 * dns_rr (struct) populated with its contents. Update the value
	 * pointed to by indexp to the next value beyond the resource record.
	 *
	 * INPUT:  wire: a pointer to an array of bytes
	 * INPUT:  indexp: a pointer to the index in the wire where the
	 *              wire-formatted resource record begins
	 * INPUT:  query_only: a boolean value (1 or 0) which indicates whether
	 *              we are extracting a full resource record or only a
	 *              query (i.e., in the question section of the DNS
	 *              message).  In the case of the latter, the ttl,
	 *              rdata_len, and rdata are skipped.
	 * OUTPUT: the resource record (struct)
	 */
	// ignore query_only
	dns_rr_ttl ttl = ntohl(*((uint32_t *)(wire + *indexp))); //not actually accurate?
	*indexp += 4;
	uint16_t rdata_len = read_2_bytes(wire, (size_t *)indexp);
	unsigned char *rdata = malloc(BUF_SIZE);
	size_t i = 0;
	for (i = 0; i < rdata_len; i++) {
		rdata[i] = wire[*indexp];
		(*indexp)++;
	}
	dns_rr full_record;
	full_record.name = "";
	full_record.type = 0;
	full_record.class = 0;
	full_record.ttl = ttl;
	full_record.rdata_len = rdata_len;
	full_record.rdata = rdata;
	return full_record;
}

int rr_to_wire(dns_rr rr, unsigned char *wire, int query_only) { // unused
	/* 
	 * Convert a DNS resource record struct to DNS wire format, using the
	 * provided byte array (wire).  Return the number of bytes used by the
	 * name in wire format.
	 *
	 * INPUT:  rr: the dns_rr struct containing the rr record
	 * INPUT:  wire: a pointer to the array of bytes where the
	 *             wire-formatted resource record should be constructed
	 * INPUT:  query_only: a boolean value (1 or 0) which indicates whether
	 *              we are constructing a full resource record or only a
	 *              query (i.e., in the question section of the DNS
	 *              message).  In the case of the latter, the ttl,
	 *              rdata_len, and rdata are skipped.
	 * OUTPUT: the length of the wire-formatted resource record.
	 *
	 */
}

unsigned short create_dns_query(char *qname, dns_rr_type qtype, unsigned char *wire) {
	/* 
	 * Create a wire-formatted DNS (query) message using the provided byte
	 * array (wire).  Create the header and question sections, including
	 * the qname and qtype.
	 *
	 * INPUT:  qname: the string containing the name to be queried
	 * INPUT:  qtype: the integer representation of type of the query (type A == 1)
	 * INPUT:  wire: the pointer to the array of bytes where the DNS wire
	 *               message should be constructed
	 * OUTPUT: the length of the DNS wire message
	 */
	int offset = 0;
	
	srandom((unsigned int)time(NULL));
	dns_query_id id = rand();
	memcpy(wire, &id, sizeof(dns_query_id));
	offset += sizeof(dns_query_id);

	dns_flags flags = htons(0x0100);
	memcpy(wire + offset, &flags, sizeof(dns_flags));
	offset += sizeof(dns_flags);

	unsigned short total_q = htons(0x0001);
	memcpy(wire + offset, &total_q, sizeof(unsigned short));
	offset += sizeof(unsigned short);

	unsigned short total_ans = htons(0x0000);
	memcpy(wire + offset, &total_ans, sizeof(unsigned short));
	offset += sizeof(unsigned short);

	unsigned short auth_records = htons(0x0000);
	memcpy(wire + offset, &auth_records, sizeof(unsigned short));
	offset += sizeof(unsigned short);

	unsigned short additional_records = htons(0x0000);
	memcpy(wire + offset, &additional_records, sizeof(unsigned short));
	offset += sizeof(unsigned short);

	int size = name_ascii_to_wire(qname, wire + offset);

	dns_rr_type query_type = htons(qtype);
	memcpy(wire + offset + size, &query_type, sizeof(dns_rr_type));
	offset += sizeof(dns_rr_type);

	dns_rr_class qclass = htons(0x0001);
	memcpy(wire + offset + size, &qclass, sizeof(dns_rr_class));
	offset += sizeof(dns_rr_class);
	
	return (unsigned short)(size + offset);
}

dns_answer_entry *get_answer_address(char *qname, dns_rr_type qtype, unsigned char *wire) {
	/* 
	 * Extract the IPv4 address from the answer section, following any
	 * aliases that might be found, and return the string representation of
	 * the IP address.  If no address is found, then return NULL.
	 *
	 * INPUT:  qname: the string containing the name that was queried
	 * INPUT:  qtype: the integer representation of type of the query (type A == 1)
	 * INPUT:  wire: the pointer to the array of bytes representing the DNS wire message
	 * OUTPUT: a linked list of dns_answer_entrys the value member of each
	 * reflecting either the name or IP address.  If
	 */
	size_t index = 4; // index of question length
	size_t num_questions = read_2_bytes(wire, &index); // unnecessary to store? Should always be 1
	size_t num_answers = read_2_bytes(wire, &index);
	if (num_answers == 0) return NULL;
	dns_answer_entry *final_list = malloc(sizeof(dns_answer_entry));

	index += 4; // skip authority and additional rrs
	char *query_name = name_ascii_from_wire(wire, (int *)(&index));
	index += strlen(query_name) + 2; // skip null terminating byte and go to after
	free(query_name);
	read_2_bytes(wire, &index); // don't worry about question type
	read_2_bytes(wire, &index); // don't worry about question class
	char *record_name;
	dns_answer_entry *current = final_list;
	char *compare_name = qname;

	for (size_t i = 0; i < num_answers; i++) {
		record_name = name_ascii_from_wire(wire, (int *)(&index));
		index += 2; // skip the c0 0c thing
		if (strcmp(record_name, compare_name) == 0) {
			free(record_name);
			dns_rr_type record_type = read_2_bytes(wire, &index);
			dns_rr_class record_class = read_2_bytes(wire, &index);
			if (qtype == record_type) {
				dns_rr full_record = rr_from_wire(wire, (int *)(&index), 0);
				dns_rdata_len rdata_len = full_record.rdata_len;
				int address_family = (rdata_len == 4) ? AF_INET : AF_INET6; // technically should change what comes after if it was AF_INET6 but don't have to worry about it for this lab.
				char *dst = malloc(sizeof(char) * 16);
				inet_ntop(address_family, full_record.rdata, dst, 16);
				current->value = dst;
				free(full_record.rdata);
			}
			else if (record_type == 5) {
				index += 4; // skip TTL
				uint16_t rdata_len = read_2_bytes(wire, &index);
				current->value = name_ascii_from_wire(wire, (int *)(&index));
				index += rdata_len;
				compare_name = current->value;
			}
			if (i < num_answers - 1) {
				current->next = malloc(sizeof(dns_answer_entry));
				current = current->next;
			}
			else {
				current->next = NULL;
			}
		}
		else {
			return NULL;
		}
	}
	return final_list;
}

int send_recv_message(unsigned char *request, int requestlen, unsigned char *response, char *server, char* port) {
	/* 
	 * Send a message (request) over UDP to a server (server) and port
	 * (port) and wait for a response, which is placed in another byte
	 * array (response).  Create a socket, "connect()" it to the
	 * appropriate destination, and then use send() and recv();
	 *
	 * INPUT:  request: a pointer to an array of bytes that should be sent
	 * INPUT:  requestlen: the length of request, in bytes.
	 * INPUT:  response: a pointer to an array of bytes in which the
	 *             response should be received
	 * OUTPUT: the size (bytes) of the response received
	 */
	struct addrinfo hints;
	struct addrinfo *result, *rp;
	int sfd, s, j;
	size_t len;
	ssize_t nread;

	memset(&hints, 0, sizeof(struct addrinfo));
	hints.ai_family = AF_UNSPEC;    /* Allow IPv4 or IPv6 */
	hints.ai_socktype = SOCK_DGRAM; /* Datagram socket */
	hints.ai_flags = 0;
	hints.ai_protocol = 0;          /* Any protocol */

	s = getaddrinfo(server, port, &hints, &result);
	if (s != 0) {
		fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(s));
		exit(EXIT_FAILURE);
	}

	for (rp = result; rp != NULL; rp = rp->ai_next) {
		sfd = socket(rp->ai_family, rp->ai_socktype,
				rp->ai_protocol);
		if (sfd == -1)
			continue;

		if (connect(sfd, rp->ai_addr, rp->ai_addrlen) != -1)
			break;                  /* Success */

		close(sfd);
	}

	if (rp == NULL) {
		fprintf(stderr, "Could not connect\n");
		exit(EXIT_FAILURE);
	}

	freeaddrinfo(result);

	if (write(sfd, request, requestlen) != requestlen) {
		fprintf(stderr, "partial/failed write\n");
		exit(EXIT_FAILURE);
	}
	// print_bytes(request, requestlen);

	nread = read(sfd, response, BUF_SIZE);
	if (nread == -1) {
		perror("read");
		exit(EXIT_FAILURE);
	}

	// printf("\n\n\n");
	// print_bytes(response, nread);

	return nread;
}

dns_answer_entry *resolve(char *qname, char *server, char *port) {
	unsigned char wire[BUF_SIZE];
	unsigned char response[BUF_SIZE];
	dns_rr_type qtype = 0x0001;
	unsigned short qsize = create_dns_query(qname, qtype, wire);
	int bytes_received = send_recv_message(wire, qsize, response, server, port);
	return get_answer_address(qname, qtype, response);
}

int main(int argc, char *argv[]) {
	char *port;
	dns_answer_entry *ans_list, *ans;
	if (argc < 3) {
		fprintf(stderr, "Usage: %s <domain name> <server> [ <port> ]\n", argv[0]);
		exit(1);
	}
	if (argc > 3) {
		port = argv[3];
	} else {
		port = "53";
	}
	ans = ans_list = resolve(argv[1], argv[2], port);
	while (ans != NULL) {
		printf("%s\n", ans->value);
		ans = ans->next;
	}
	if (ans_list != NULL) {
		free_answer_entries(ans_list);
	}

	return 0;
}
