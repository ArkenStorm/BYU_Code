#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <stack>
#include <vector>
#include <iomanip>

#include "ExpressionManagerInterface.h"
#include "ExpressionManager.h"

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

	string expressionLine;
	ExpressionManager expressionConverter;

	while (!in.eof())
	{
		getline(in, expressionLine);
		if (expressionLine == "")
		{
			break;
		}
		out << expressionLine << endl;
		expressionLine.erase(0, 12);						//gets rid of "Expression: " in the string

		expressionConverter.setExpression(expressionLine);

		out << setw(12) << "Infix: " << expressionConverter.infix() << endl;
		if (expressionConverter.infix().substr(0, 6) == "Caught")				//skips to the next line if there was an exception thrown
		{
			out << endl;
			continue;
		}

		out << setw(12) << "Postfix: " << expressionConverter.postfix() << endl;
		out << setw(12) << "Prefix: " << expressionConverter.prefix() << endl;
		out << setw(12) << "Value: " << expressionConverter.value() << endl << endl;
	}

	return 0;
}