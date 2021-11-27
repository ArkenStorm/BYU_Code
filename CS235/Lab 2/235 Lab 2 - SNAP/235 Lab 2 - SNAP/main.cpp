#include <iostream>
#include <string>
#include <limits>
#include <fstream>
#include <sstream>
#include <vector>

using namespace std;

#include "SNAP.h"
#include "CSG.h"
#include "CDH.h"
#include "CR.h"
#include "Course.h"

#define CONSOLE 1

#ifdef _MSC_VER
#define _CRTDBG_MAP_ALLOC  
#include <crtdbg.h>
#define VS_MEM_CHECK _CrtSetDbgFlag(_CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF);
#else
#define VS_MEM_CHECK
#endif


int main(int argc, char* argv[])
{
	vector<Snap> snapVector;
	vector<Csg> csgVector;
	vector<Cdh> cdhVector;
	vector<Cr> crVector;

	VS_MEM_CHECK;

	if (argc < 3)
	{
		cerr << "Please provide name of input and output files";
		return 1;
	}
	cout << "Input file: " << argv[1] << endl;
	ifstream in(argv[1]);
	if (!in)
	{
		cerr << "Unable to open " << argv[1] << " for input";
		return 1;
	}
	cout << "Output file: " << argv[2] << endl;
	ofstream out(argv[2]);
	if (!out)
	{
		in.close();
		cerr << "Unable to open " << argv[2] << " for output";
	}

	out << "Input Strings: " << endl;
	
	for (string inputLine; getline(in, inputLine);)
	{
		try
		{
			if ("cr(" == inputLine.substr(0, 3))
			{
				out << inputLine << endl;
				string courseName = inputLine.substr(3, inputLine.find(',') - 3);
				inputLine = inputLine.substr(inputLine.find(',') + 1);
				string room = inputLine.substr(0, inputLine.find(')'));
				crVector.push_back(Cr(courseName, room));
			}

			else if ("cdh(" == inputLine.substr(0, 4))
			{
				out << inputLine << endl;
				string courseName = inputLine.substr(4, inputLine.find(',') - 4);
				inputLine = inputLine.substr(inputLine.find(',') + 1);
				string weekDay = inputLine.substr(0, inputLine.find(','));
				inputLine = inputLine.substr(inputLine.find(',') + 1);
				string timeOfDay = inputLine.substr(0, inputLine.find(')'));
				cdhVector.push_back(Cdh(courseName, weekDay, timeOfDay));
			}

			else if ("csg(" == inputLine.substr(0, 4))
			{
				out << inputLine << endl;
				string courseName = inputLine.substr(4, inputLine.find(',') - 4);
				inputLine = inputLine.substr(inputLine.find(',') + 1);
				string studentID = inputLine.substr(0, inputLine.find(','));
				inputLine = inputLine.substr(inputLine.find(',') + 1);
				string grade = inputLine.substr(0, inputLine.find(')'));
				csgVector.push_back(Csg(courseName, studentID, grade));
			}

			else if ("snap(" == inputLine.substr(0, 5))
			{
				out << inputLine << endl;
				string studentID = inputLine.substr(5, inputLine.find(',') - 5);
				inputLine = inputLine.substr(inputLine.find(',') + 1);
				string studentName = inputLine.substr(0, inputLine.find(','));
				inputLine = inputLine.substr(inputLine.find(',') + 1);
				string studentAddress = inputLine.substr(0, inputLine.find(','));
				inputLine = inputLine.substr(inputLine.find(',') + 1);
				string studentPhone = inputLine.substr(0, inputLine.find(')'));
				snapVector.push_back(Snap(studentID, studentName, studentAddress, studentPhone));
			}

			else throw inputLine;
		}
		catch (string error)
		{
			out << error << " ** Undefined " << error << endl;
		}
	}

	out << endl << "Vectors: " << endl;

	for (size_t i = 0; i < snapVector.size(); ++i)
	{
		out << snapVector.at(i);
	}

	for (size_t i = 0; i < csgVector.size(); ++i)
	{
		out << csgVector.at(i);
	}

	for (size_t i = 0; i < cdhVector.size(); ++i)
	{
		out << cdhVector.at(i);
	}

	for (size_t i = 0; i < crVector.size(); ++i)
	{
		out << crVector.at(i);
	}

	return 0;
}