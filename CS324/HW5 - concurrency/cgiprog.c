#include <stdio.h>
#include <string.h>

#define MAXLINE 1024

int main(void) {
    char *buf, *p;
    char content[MAXLINE];
    buf = getenv("QUERY_STRING");

    /* Make the response body */
    sprintf(content, "The query string is: %s\n", buf);
  
    /* Generate the HTTP response */
    printf("Connection: close\r\n");
    printf("Content-length: %d\r\n", (int)strlen(content));
    printf("Content-type: text/plain\r\n\r\n");
    printf("%s", content);
    fflush(stdout);

    exit(0);
}