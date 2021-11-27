#ifndef PARSER_H
#define PARSER_H

#include <stdio.h>

#define HEADER_NAME_MAX_SIZE 512
#define HEADER_VALUE_MAX_SIZE 1024

typedef struct {
	char name[HEADER_NAME_MAX_SIZE];
	char value[HEADER_VALUE_MAX_SIZE];
} http_header;

int is_complete_request(const char *request);
int parse_request(const char *request, char *method, char *hostname, char *port, char *uri, http_header *headers);
char *get_header_value(const char *name, http_header *headers, int num_headers);
int add_header(char *name, char *value, http_header *headers, int num_headers);

#endif