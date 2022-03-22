#include <stdio.h>
#include <string.h>
#include "parser.h"

#define MAX_HEADERS 32

/* Input: request, a string
 * Output: 1 if request is a complete HTTP request, 0 otherwise
 * */
int is_complete_request(const char *request) {
	return !!strstr(request, "\r\n\r\n");
}

/* Parse an HTTP request, and copy each parsed value into the
 * corresponding array as a NULL-terminated string.
 * Input: request - string containing the original request;
 *        should not be modifed.
 * Input: method, hostname, port, uri - arrays to which the
 *        corresponding parts parsed from the request should be
 *        copied, as strings.  The uri is the "file" part of the requested URL.
 *        If no port is specified, the port array should be populated with a
 *        string specifying the default HTTP port.
 * Input: headers - an array of http_headers, each of which should be
 *        populated with the corresponding name/value of a header.
 * Output: return the number of headers in the request.
 * */
int parse_request(const char *request, char *method, char *hostname, char *port, char *uri, http_header *headers) {
	int num_headers = 0;
	char *request_copy = strdup(request);
	printf("request copy: %s\n", request_copy);
	char *request_line = strtok(request_copy,"\r\n");  // get the first line of the request

	char *req_ptr;
	strcpy(method,strtok_r(request_line," ",&req_ptr));  // Obtains the GET from the 1st request line

	char url[1024];
	strcpy(url,strtok_r(NULL," ",&req_ptr));  // obtains the URL - req_ptr helps the tokenizer continue from the last call

	strcpy(url, strstr(url, "//") + 2);  // move the ptr past the "http://"

	char *uri_ptr = strchr(url, '/');
	if (uri_ptr != NULL) {
		strcpy(uri, uri_ptr);
	}

	char *url_ptr;
	strcpy(hostname, strtok_r(url,"/", &url_ptr)); // pull out the host name and optional port

	char *port_ptr = strchr(hostname, ':');
	if (port_ptr != NULL) {
		strcpy(port, port_ptr + 1);
	}
	else {
		strcpy(port, "80");
	}

	char *host_ptr;
	strtok_r(hostname, ":", &host_ptr); // just the hostname, not the port too

	// after parsing the first line
	while ((request_line = strtok(NULL,"\r\n")) != NULL) { // get subsequent lines until it returns NULL
		char *headers_ptr;
		char name[HEADER_NAME_MAX_SIZE];
		char value[HEADER_VALUE_MAX_SIZE];
		strcpy(name, strtok_r(request_line, ":", &headers_ptr));
		strcpy(value, strtok_r(NULL, ":", &headers_ptr));
		http_header header;
		strcpy(header.name, name);
		strcpy(header.value, value);
		headers[num_headers] = header;
		num_headers++;
	}

	return num_headers;
}

/* Iterate through an array of headers, and return the value
 * (as a char *) corresponding to the name passed.  If there is no
 * header with the name passed, return NULL.
 * Input: name - the name of the header whose value is being sought.
 * Input: headers - the array of http_headers to be searched.
 * Input: num_headers - the number of headers in the headers array.
 * */
char *get_header_value(const char *name, http_header *headers, int num_headers) {
	char *header_val = NULL;
	for (size_t i = 0; i < num_headers; i++) {
		if (strcmp(headers[i].name, name) == 0) {
			header_val = headers[i].value;
		}
	}
	return header_val;
}

int add_header(char *name, char *value, http_header *headers, int num_headers) {
    if (get_header_value(name, headers, num_headers) == NULL) {
        http_header new_header;
        strcpy(new_header.name, name);
		strcpy(new_header.value, value);
		headers[num_headers] = new_header;
		num_headers++;
    }
    return num_headers;
}