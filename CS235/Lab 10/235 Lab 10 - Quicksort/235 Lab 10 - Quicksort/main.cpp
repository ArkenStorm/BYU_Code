#include <iostream>
#include <string>
#include <sstream>
#include <fstream>
#include "QuickSort.h"
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
	QuickSort<int> QuickSortArray;
	istringstream inSS;
	string commandLine;
	string currentCommand;
	size_t inputCapacity = 0;
	size_t leftIndex = 0;
	size_t rightIndex = 0;
	size_t pivotIndex = 0;
	bool functionBool = false;

	while (!in.eof())
	{
		getline(in, commandLine);

		if (commandLine == "") { continue; }

		out << commandLine;
		inSS.clear();
		inSS.str(commandLine);

		inSS >> currentCommand;

		if (currentCommand == "QuickSort")
		{
			inSS >> inputCapacity;
			functionBool = QuickSortArray.createArray(inputCapacity);
			if (functionBool == true) { out << " OK" << endl; }

			else { out << " Error" << endl; }
		}

		else if (currentCommand == "AddToArray")
		{
			int newElement = 0;
			while (inSS >> newElement) { functionBool = QuickSortArray.addElement(newElement); }

			if (functionBool == true) {	out << " OK" << endl; }

			else { out << " Error" << endl; }
		}

		else if (currentCommand == "Capacity") { out << " " << QuickSortArray.capacity() << endl; }

		else if (currentCommand == "Clear")
		{
			functionBool = QuickSortArray.clear();

			if (functionBool == true) {	out << " OK" << endl; }

			else { out << " Error" << endl; }
		}

		else if (currentCommand == "Size") { out << QuickSortArray.size() << endl; }

		else if (currentCommand == "Sort")
		{
			inSS >> leftIndex;
			inSS >> rightIndex;

			functionBool = QuickSortArray.sort(leftIndex, rightIndex);

			if (functionBool == true) {	out << " OK" << endl; }

			else { out << " Error" << endl; }
		}

		else if (currentCommand == "SortAll")
		{
			functionBool = QuickSortArray.sortAll();

			if (functionBool == true) { out << " OK" << endl; }

			else { out << " Error" << endl; }
		}

		else if (currentCommand == "MedianOfThree")
		{
			inSS >> leftIndex;
			inSS >> rightIndex;

			out << leftIndex << ", " << rightIndex << " = " << QuickSortArray.medianOfThree(leftIndex, rightIndex) << endl;
		}

		else if (currentCommand == "Partition")
		{
			inSS >> leftIndex;
			inSS >> rightIndex;
			inSS >> pivotIndex;

			out << leftIndex << ", " << rightIndex << ", " << pivotIndex << ", " << QuickSortArray.partition(leftIndex, rightIndex, pivotIndex) << endl;
		}

		else if (currentCommand == "PrintArray") { out << QuickSortArray << endl; }

		else if (currentCommand == "Stats") { out << "Not Implemented Yet." << endl; }
	}

	return 0;
}