#include<stdio.h>
#include<string.h>

#define HEADER_NAME_MAX_SIZE 512
#define HEADER_VALUE_MAX_SIZE 1024
#define MAX_HEADERS 32

typedef struct {
	char name[HEADER_NAME_MAX_SIZE];
	char value[HEADER_VALUE_MAX_SIZE];
} http_header;

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
	char *request_line = strtok(request_copy,"\r\n");  // get the first line of the request

	char *req_ptr;
	strcpy(method,strtok_r(request_line," ",&req_ptr));  // Obtains the GET from the 1st request line
	
	char url[128];
	strcpy(url,strtok_r(NULL," ",&req_ptr));  // obtains the URL - req_ptr helps the tokenizer continue from the last call
	
	char *url_ptr;
	strcpy(url,strtok_r(url,"//",&url_ptr));  // move the ptr past the "http://"
	strcpy(hostname, strtok_r(NULL,"/", &url_ptr)); // pull out the host name and optional port

	char *port_ptr = strchr(hostname, ':');
	if (port_ptr != NULL) {
		strcpy(port, port_ptr + 1);
	}
	else {
		strcpy(port, "80");
	}

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


int main() {
	char s1[4096];
	sprintf(s1, "GET http://www.example.com/index.html HTTP/1.0\r\n");
	sprintf(s1, "%sHost: www.example.com\r\n", s1);
	sprintf(s1, "%sUser-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0\r\n", s1);
	sprintf(s1, "%sAccept-Language: en-US,en;q=0.5\r\n\r\n", s1);

	char s2[4096];
	sprintf(s2, "GET http://www.example.com:8080/index.html?foo=1&bar=2 HTTP/1.0\r\n");
	sprintf(s2, "%sHost: www.example.com:8080\r\n", s2);
	sprintf(s2, "%sUser-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0\r\n", s2);
	sprintf(s2, "%sAccept-Language: en-US,en;q=0.5\r\n\r\n", s2);

	char s3[4096];
	sprintf(s3, "GET http://www.example.com:8080/index.html HTTP/1.0\r\n");
	
	/* declare any vars you'll need... */
	char method[512];
	char hostname[512];
	char port[512];
	char uri[512];
	http_header headers[1024];
	int num_headers = 0;

	if (is_complete_request(s1)) {
		num_headers = parse_request(s1, method, hostname, port, uri, headers);
		/* run parse_request() on s1 with the appropriate vars you've
		 * declared, and then run the following:
		 */
		printf("s1 method: %s\n", method);
		printf("s1 hostname: %s\n", hostname);
		printf("s1 port: %s\n", port);
		printf("s1 host: %s\n", get_header_value("Host", headers, num_headers));
	} else {
		printf("s1 is incomplete\n");
	}

	if (is_complete_request(s2)) {
		num_headers = parse_request(s2, method, hostname, port, uri, headers);
		/* run parse_request() on s2 with the appropriate vars you've
		 * declared, and then run the following:
		 */
		printf("s2 method: %s\n", method);
		printf("s2 hostname: %s\n", hostname);
		printf("s2 port: %s\n", port);
		printf("s2 host: %s\n", get_header_value("Host", headers, num_headers));
	} else {
		printf("s2 is incomplete\n");
	}

	if (is_complete_request(s3)) {
		num_headers = parse_request(s3, method, hostname, port, uri, headers);
		/* run parse_request() on s3 with the appropriate vars you've
		 * declared, and then run the following:
		 */
		printf("s3 method: %s\n", method);
		printf("s3 hostname: %s\n", hostname);
		printf("s3 port: %s\n", port);
		printf("s3 host: %s\n", get_header_value("Host", headers, num_headers));
	} else {
		printf("s3 is incomplete\n");
	}

}
