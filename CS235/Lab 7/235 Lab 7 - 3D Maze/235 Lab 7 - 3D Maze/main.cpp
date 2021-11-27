#include <iostream>
#include <string>
#include <fstream>
#include <sstream>

using namespace std;

#include "Maze.h"

#ifdef _MSC_VER
#define _CRTDBG_MAP_ALLOC  
#include <crtdbg.h>
#define VS_MEM_CHECK _CrtSetDbgFlag(_CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF);
#else
#define VS_MEM_CHECK;
#endif

int main(int argc, char* argv[])
{
	VS_MEM_CHECK;

	ifstream in(argv[1]);
	ofstream out(argv[2]);
	string mazeDimensions;
	string mazeValues;
	int height = 0;
	int width = 0;
	int layers = 0;
	int cellValue = 0;
	bool mazeEnd = false;

	getline(in, mazeDimensions);
	istringstream inSS(mazeDimensions);

	inSS >> height >> width >> layers;

	Maze KobayashiMaru(height, width, layers);

	out << "Solve Maze: " << endl;

	for (int k = 0; k < layers; k++)
	{
		for (int i = 0; i < height; i++)
		{
			getline(in, mazeValues);
			if (mazeValues == "") { getline(in, mazeValues); }

			inSS.clear();
			inSS.str(mazeValues);
				
			for (int j = 0; j < width; j++)
			{
				inSS >> cellValue;
				KobayashiMaru.setValue(i, j, k, cellValue);
			}
		}
	}
	out << KobayashiMaru.toString();

	KobayashiMaru.setValue(height - 1, width - 1, layers - 1, KobayashiMaru.EXIT);

	if (KobayashiMaru.find_maze_path() == false)
	{
		out << "No Solution Exists!" << endl;
		return 0;
	}

	out << "Solution: " << endl << endl;
	out << KobayashiMaru.toString();

	return 0;
}