#include <iostream>
#include <string>
#include <sstream>
#include "Maze.h"
using namespace std;

Maze::Maze(int height, int width, int layers)
{
	mazeHeight = height;
	mazeWidth = width;
	mazeLayers = layers;

	maze3D = new int **[mazeHeight];

	for (int i = 0; i < mazeHeight; ++i)
	{
		maze3D[i] = new int *[mazeWidth];
		for (int j = 0; j < mazeWidth; ++j) { maze3D[i][j] = new int[mazeLayers]; }
	}
}

Maze::~Maze()
{
	for (int i = 0; i < mazeHeight; ++i)
	{
		for (int j = 0; j < mazeWidth; ++j) { delete[] maze3D[i][j]; }
		delete[] maze3D[i];
	}
	delete[] maze3D;
}

/** Set maze value
@parm height
@parm width
@parm layer
@parm value
*/
void Maze::setValue(int height, int width, int layer, int value)
{
	maze3D[height][width][layer] = value;
}

/** Solve maze
@return true if solveable, else false
*/
bool Maze::find_maze_path()
{
	return pathFinder(0, 0, 0);
}

/** Output maze (same order as input maze)
@return string of 2D layers
*/
string Maze::toString() const
{
	ostringstream mazeSS;

	for (int k = 0; k < mazeLayers; k++)
	{
		mazeSS << "Layer " << k + 1 << endl << endl;
		for (int i = 0; i < mazeHeight; i++)
		{
			for (int j = 0; j < mazeWidth; j++)
			{
				if (maze3D[i][j][k] == OPEN || maze3D[i][j][k] == TEMPORARY) { mazeSS << "_ "; }
				else if(maze3D[i][j][k] == BLOCKED) { mazeSS << "X "; } 
				else if (maze3D[i][j][k] == EXIT) { mazeSS << "E "; }
				else if (maze3D[i][j][k] == UP) { mazeSS << "U "; }
				else if (maze3D[i][j][k] == DOWN) { mazeSS << "D "; }
				else if (maze3D[i][j][k] == LEFT) { mazeSS << "L "; }
				else if (maze3D[i][j][k] == RIGHT) { mazeSS << "R "; }
				else if (maze3D[i][j][k] == OUT) { mazeSS << "O "; }
				else if (maze3D[i][j][k] == IN) { mazeSS << "I "; }
			}
			mazeSS << endl;
		}
		mazeSS << endl;
	}

	return mazeSS.str();
}

bool Maze::pathFinder(int height, int width, int layer)		//recursively finds the path
{
	if (height < 0 || height >= mazeHeight || width < 0 || width >= mazeWidth || layer < 0 || layer >= mazeLayers) { return false; }
	if (maze3D[height][width][layer] == EXIT) { return true; }
	if (maze3D[height][width][layer] != OPEN) { return false; }

	maze3D[height][width][layer] = TEMPORARY;
	if (pathFinder(height, width - 1, layer))
	{
		maze3D[height][width][layer] = LEFT;
		return true;
	}
		
	else if (pathFinder(height, width + 1, layer))
	{
		maze3D[height][width][layer] = RIGHT;
		return true;
	}
		
	else if (pathFinder(height - 1, width, layer))
	{
		maze3D[height][width][layer] = UP;
		return true;
	}

	else if (pathFinder(height + 1, width, layer))
	{
		maze3D[height][width][layer] = DOWN;
		return true;
	}

	else if (pathFinder(height, width, layer - 1))
	{
		maze3D[height][width][layer] = OUT;
		return true;
	}

	else if (pathFinder(height, width, layer + 1))
	{
		maze3D[height][width][layer] = IN;
		return true;
	}

	return false;
}