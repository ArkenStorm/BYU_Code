#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
#include <stdlib.h>
#include <limits>
#include "LinkedList.h"
#include "LinkedListInterface.h"

using namespace std;

#ifdef _MSC_VER
#define _CRTDBG_MAP_ALLOC  
#include <crtdbg.h>
#define VS_MEM_CHECK _CrtSetDbgFlag(_CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF);
#else
#define VS_MEM_CHECK;
#endif


int main(int argc, char * argv[])
{
	VS_MEM_CHECK;
	
	ifstream in(argv[1]);
	ofstream out(argv[2]);

	string listType;
	string firstLine;
	getline(in, firstLine);
	istringstream inSS(firstLine);

	inSS >> listType;
	inSS.clear();
	out << listType << " [" << listType << "]" << endl;

	string mode = "none";
	LinkedList<int> intList;
	LinkedList<string> stringList;

	if (listType == "INT") { mode = listType; }
	else if (listType == "STRING") { mode = listType; }

	int iParamOne = 0;				// variables to read in
	int iParamTwo = 0;
	string sParamOne = "none";
	string sParamTwo = "none";
	int index = 0;
	string boolValue = "none";
	string command;
	string commandLine;

	while (!in.eof())
	{
		getline(in, commandLine);
		inSS.clear();
		inSS.str(commandLine);
		inSS >> command;
		out << command << " ";
		
		if (mode == "INT")
		{
			if (command == "insertHead")
			{
				inSS >> iParamOne;
				out << iParamOne << " ";
				if (intList.insertHead(iParamOne) == 0) { boolValue = "false"; }
				else { boolValue = "true"; }

				out << boolValue << endl;
			}

			else if (command == "insertTail")
			{
				inSS >> iParamOne;
				out << iParamOne << " ";
				if (intList.insertTail(iParamOne) == 0) { boolValue = "false"; }
				else { boolValue = "true"; }
				out << boolValue << endl;
			}

			else if (command == "insertAfter")
			{
				inSS >> iParamOne >> iParamTwo;
				out << iParamOne << " " << iParamTwo << " ";
				if (intList.insertAfter(iParamOne, iParamTwo) == 0) { boolValue = "false"; }
				else { boolValue = "true"; }
				out << boolValue << endl;
			}

			else if (command == "remove")
			{
				inSS >> iParamOne;
				out << iParamOne << " ";
				if (intList.remove(iParamOne) == 0) { boolValue = "false"; }
				else { boolValue = "true"; }
				out << boolValue << endl;
			}

			else if (command == "clear")
			{
				if (intList.clear() == 0) { boolValue = "false"; }
				else { boolValue = "true"; }
				out << boolValue << endl;
			}

			else if (command == "at")
			{
				inSS >> index;
				out << index << " " << intList.at(index) << endl;
			}

			else if (command == "size")
			{
				out << intList.size() << endl;
			}

			else if (command == "printList")
			{
				out << intList.PrintList() << endl;
			}
		}

		else if (mode == "STRING")
		{
			if (command == "insertHead")
			{
				inSS >> sParamOne;
				out << sParamOne << " ";
				if (stringList.insertHead(sParamOne) == 0) { boolValue = "false"; }
				else { boolValue = "true"; }
				out << boolValue << endl;
			}

			else if (command == "insertTail")
			{
				inSS >> sParamOne;
				out << sParamOne << " ";
				if (stringList.insertTail(sParamOne) == 0) { boolValue = "false"; }
				else { boolValue = "true"; }
				out << boolValue << endl;
			}

			else if (command == "insertAfter")
			{
				inSS >> sParamOne >> sParamTwo;
				out << sParamOne << " " << sParamTwo << " ";
				if (stringList.insertAfter(sParamOne, sParamTwo) == 0) { boolValue = "false"; }
				else { boolValue = "true"; }
				out << boolValue << endl;
			}

			else if (command == "remove")
			{
				inSS >> sParamOne;
				out << sParamOne << " ";
				if (stringList.remove(sParamOne) == 0) { boolValue = "false"; }
				else { boolValue = "true"; }
				out << boolValue << endl;
			}

			else if (command == "clear")
			{
				if (stringList.clear() == 0) { boolValue = "false"; }
				else { boolValue = "true"; }
				out << boolValue << endl;
			}

			else if (command == "at")
			{
				inSS >> index;
				out << index << " " << stringList.at(index) << endl;
			}

			else if (command == "size")
			{
				out << stringList.size() << endl;
			}

			else if (command == "printList")
			{
				out << stringList.PrintList() << endl;
			}
		}
	}



	return 0;
}