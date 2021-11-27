/**
 * Virtual cores: 12
 * 
 * Times:
 * 1 Thread: 34.293032
 * 2 Threads: 17.983646
 * 4 Threads: 10.095961
 * 8 Threads: 5.794461
*/

/*
	This program is an adaptation of the Mandelbrot program
	from the Programming Rosetta Stone, see
	http://rosettacode.org/wiki/Mandelbrot_set
	Compile the program with:
	gcc -o mandelbrot -O4 mandelbrot.c
	Usage:
 
	./mandelbrot <xmin> <xmax> <ymin> <ymax> <maxiter> <xres> <out.ppm>
	Example:
	./mandelbrot 0.27085 0.27100 0.004640 0.004810 1000 1024 pic.ppm
	The interior of Mandelbrot set is black, the levels are gray.
	If you have very many levels, the picture is likely going to be quite
	dark. You can postprocess it to fix the palette. For instance,
	with ImageMagick you can do (assuming the picture was saved to pic.ppm):
	convert -normalize pic.ppm pic.png
	The resulting pic.png is still gray, but the levels will be nicer. You
	can also add colors, for instance:
	convert -negate -normalize -fill blue -tint 100 pic.ppm pic.png
	See http://www.imagemagick.org/Usage/color_mods/ for what ImageMagick
	can do. It can do a lot.
*/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <stdint.h>
#include <string.h>
#include <omp.h>

int main(int argc, char* argv[])
{
	/* Parse the command line arguments. */
	if (argc != 8) {
		printf("Usage:   %s <xmin> <xmax> <ymin> <ymax> <maxiter> <xres> <out.ppm>\n", argv[0]);
		printf("Example: %s 0.27085 0.27100 0.004640 0.004810 1000 1024 pic.ppm\n", argv[0]);
		exit(EXIT_FAILURE);
	}

	/* The window in the plane. */
	const double xmin = atof(argv[1]);
	const double xmax = atof(argv[2]);
	const double ymin = atof(argv[3]);
	const double ymax = atof(argv[4]);

	/* Maximum number of iterations, at most 65535. */
	const uint16_t maxiter = (unsigned short)atoi(argv[5]);

	/* Image size, width is given, height is computed. */
	const int xres = atoi(argv[6]);
	const int yres = (xres*(ymax-ymin))/(xmax-xmin);

	/* The output file name */
	const char* filename = argv[7];

	/* Open the file and write the header. */
	FILE * fp = fopen(filename,"wb");
	// char *comment="# Mandelbrot set";/* comment should start with # */

	/*write ASCII header to the file*/
	fprintf(fp,
					"P6\n# Mandelbrot, xmin=%lf, xmax=%lf, ymin=%lf, ymax=%lf, maxiter=%d\n%d\n%d\n%d\n",
					xmin, xmax, ymin, ymax, maxiter, xres, yres, (maxiter < 256 ? 256 : maxiter));

	/* Precompute pixel width and height. */
	double dx=(xmax-xmin)/xres;
	double dy=(ymax-ymin)/yres;

	double x, y; /* Coordinates of the current point in the complex plane. */
	int j; /* Pixel counter */
	int k; /* Iteration counter */
	double start, end;

	unsigned char *buffer = malloc(xres*yres*6);

	start = omp_get_wtime();
	#pragma omp parallel for num_threads(8) private(j,k,x,y)
	for (j = 0; j < yres; j++) {
		y = ymax - j * dy;
		for(int i = 0; i < xres; i++) {
			double u, v; /* Coordinates of the iterated point. */
			u = 0.0;
			v = 0.0;
			double u2 = u * u;
			double v2 = v*v;
			x = xmin + i * dx;
			/* iterate the point */
			for (k = 1; k < maxiter && (u2 + v2 < 4.0); k++) {
						v = 2 * u * v + y;
						u = u2 - v2 + x;
						u2 = u * u;
						v2 = v * v;
			}
			/* compute  pixel color and write it to file */
			if (k >= maxiter) {
				/* interior */
				const unsigned char black[] = {0, 0, 0, 0, 0, 0};
				// write to the buffer
				int offset = ((j * xres + i) * 6);
				memcpy(buffer + offset, black, 6);
			}
			else {
				/* exterior */
				unsigned char color[6];
				color[0] = k >> 8;
				color[1] = k & 255;
				color[2] = k >> 8;
				color[3] = k & 255;
				color[4] = k >> 8;
				color[5] = k & 255;
				// write to buffer
				int offset = ((j * xres + i) * 6);
				memcpy(buffer + offset, color, 6);
			}
		}
	}
	end = omp_get_wtime();
	printf("TIME: %f\n", end-start);

	fwrite(buffer, 6, xres*yres, fp); // write to file; 

	fclose(fp);
	return 0;
}