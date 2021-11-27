#include <iostream>
#include <string>
#include <fstream>
#include <sstream>

using namespace std;

#include "Deque.h"
#include "Station.h"

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

	string commandLine;
	string trainCommand;
	size_t trainNumber;
	istringstream inSS;
	
	Station<size_t> GrandCentralStation;

	while (!in.eof())
	{
		getline(in, commandLine);

		if (commandLine == "")		//checks if there's an extra line at the end of the input file
		{
			continue;
		}

		out << commandLine;			

		inSS.clear();
		inSS.str(commandLine);
		inSS >> trainCommand;
		inSS >> trainNumber;

		if (trainCommand == "Add:station") { out << " " << GrandCentralStation.addCar(trainNumber) << endl; }
		else if (trainCommand == "Add:queue") { out << " " << GrandCentralStation.addQueue() << endl; }
		else if (trainCommand == "Add:stack") { out << " " << GrandCentralStation.addStack() << endl; }
		else if (trainCommand == "Remove:station") { out << " " << GrandCentralStation.removeCar() << endl; }
		else if (trainCommand == "Remove:queue") { out << " " << GrandCentralStation.removeQueue() << endl; }
		else if (trainCommand == "Remove:stack") { out << " " << GrandCentralStation.removeStack() << endl; }
		else if (trainCommand == "Top:station") { out << " " << GrandCentralStation.topCar() << endl; }
		else if (trainCommand == "Top:queue") { out << " " << GrandCentralStation.topQueue() << endl; }
		else if (trainCommand == "Top:stack") { out << " " << GrandCentralStation.topStack() << endl; }
		else if (trainCommand == "Size:queue") { out << " " << GrandCentralStation.sizeQueue() << endl; }
		else if (trainCommand == "Size:stack") { out << " " << GrandCentralStation.sizeStack() << endl; }
		else if (trainCommand.substr(0,5) == "Find:")
		{
			inSS.clear();
			inSS.str(trainCommand.substr(5));
			inSS >> trainNumber;
			out << " " << GrandCentralStation.find(trainNumber) << endl;
		}

		else if (trainCommand == "Train:")
		{
			out << " " << GrandCentralStation.toString() << endl;
		}
	}

	return 0;
}