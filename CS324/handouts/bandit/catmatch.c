#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>

int main(int argc, char* argv[]) {
	FILE* file = fopen(argv[1], "r");
	fprintf(stderr, "%d\n\n", getpid());

	const char* ENV_VAR = "CATMATCH_PATTERN";
	const char* PATTERN_VAL = getenv(ENV_VAR);

	int bufferSize = 1024;
	char buffer[bufferSize];

	while (fgets(buffer, bufferSize, file)) {
		int hasMatch = 0;
		if (PATTERN_VAL && PATTERN_VAL[0]) {
			if (strstr(buffer, PATTERN_VAL)) {
				hasMatch = 1;
			}
		}
		printf("%d %s\n", hasMatch, buffer);
	}

	return 0;
}