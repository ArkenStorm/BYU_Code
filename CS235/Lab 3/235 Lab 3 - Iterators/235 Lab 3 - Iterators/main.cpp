#include <iostream>
#include <string>
#include <vector>
#include <stdlib.h>
#include <fstream>
#include <sstream>
#include "MyArray.h"

using namespace std;

#ifdef _MSC_VER
#define _CRTDBG_MAP_ALLOC  
#include <crtdbg.h>
#define VS_MEM_CHECK _CrtSetDbgFlag(_CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF);
#else
#define VS_MEM_CHECK
#endif

const int MAX_ARRAY_SIZE = 1000;
bool isPrime(int number);

int main(int argc, char * argv[])
{
	VS_MEM_CHECK;

	MyArray<int> inputNumbers(MAX_ARRAY_SIZE);

	if (argc < 3)
	{
		cerr << "Please provide name of input and output files";
		return 1;
	}
	cout << "Input file: " << argv[1] << endl;
	ifstream inFS(argv[1]);
	if (!inFS)
	{
		cerr << "Unable to open " << argv[1] << " for input";
		return 1;
	}
	cout << "Output file: " << argv[2] << endl;
	ofstream out(argv[2]);
	if (!out)
	{
		inFS.close();
		cerr << "Unable to open " << argv[2] << " for output";
	}

	int i;
	ifstream in(argv[1]);
	while (in >> i)
	{
		inputNumbers.push_back(i);
	}

	out << "myArray" << endl;
	out << inputNumbers << endl << endl;

	out << "SEQUENTIAL" << endl;
	MyArray<int>::Iterator iter1 = inputNumbers.begin();
	out << "iter1: " << iter1 << endl;
	for (; iter1 != inputNumbers.end(); ++iter1)
		out << *iter1 << " ";
	out << endl << endl;

	out << "PRIME (Bonus)" << endl;
	for (size_t i = 0; i < inputNumbers.size(); i++)
	{
		if (isPrime(inputNumbers[i])) out << inputNumbers[i] << " ";
	}
	out << endl << endl;

	out << "COMPOSITE (Bonus)" << endl;
	for (size_t i = 0; i < inputNumbers.size(); i++)
	{
		if (!isPrime(inputNumbers[i])) out << inputNumbers[i] << " ";
	}
	out << endl << endl;

	out << "FIBONACCI (Bonus)" << endl;
	MyArray<int>::Iterator iter2 = inputNumbers.begin();
	out << "iter2: " << iter2 << endl;
	int n1 = *iter2;
	iter2++;
	int n2 = *iter2;
	iter2++;

	while (iter2 != inputNumbers.end())
	{
		if ((n1 + n2) == *iter2)
		{
			out << *iter2 << "=" << n1 << "+" << n2 << " ";
		}
		n1 = n2;
		n2 = *iter2;
		iter2++;
	}
	out << endl << endl;

	return 0;
}

bool isPrime(int number)
{
	int i;
	if (number < 2) return false;
	for (i = 2; i < number; ++i)
	{
		if (number % i == 0) return false;
	}
	return true;
}