#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
#include "BST.h"

using namespace std;

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

	BST<int> Yggdrasil;
	string commandLine;
	string treeCommand;
	istringstream inSS;
	int nodeValue;


	while (!in.eof())
	{
		getline(in, commandLine);
		if (commandLine == "")
		{
			return 0;
		}

		inSS.clear();
		inSS.str(commandLine);
		inSS >> treeCommand;

		if (treeCommand == "Add")
		{
			inSS >> nodeValue;
			out << commandLine;
			if (Yggdrasil.addNode(nodeValue) == true)
			{
				out << " true" << endl;
			}

			else
			{
				out << " false" << endl;
			}
		}

		else if (treeCommand == "Remove")
		{
			inSS >> nodeValue;
			out << commandLine;
			if (Yggdrasil.removeNode(nodeValue) == true)
			{
				out << " true" << endl;
			}

			else
			{
				out << " false" << endl;
			}
		}

		else if (treeCommand == "Clear")
		{
			out << commandLine;
			if (Yggdrasil.clearTree() == true)
			{
				out << " true" << endl;
			}

			else
			{
				out << " false" << endl;
			}
		}

		else if (treeCommand == "PrintBST")
		{
			out << commandLine;
			out << Yggdrasil.toString() << endl;
		}
	}


	return 0;
}