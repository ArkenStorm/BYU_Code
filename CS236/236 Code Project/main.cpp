#include <iostream>
#include "Lexer.h"
#include "Parser.h"
#include "Interpreter.h"

using namespace std;

int main(int argc, char* argv[]) {
	Lexer Lexus(argv[1]);
	Lexus.ScanTokens();
	//cout << Lexus.PrintAllTokens();
	Parser DatalogParser(Lexus.GetTokenList());
	DatalogParser.Parse();
	Interpreter Translator(DatalogParser.GetProgram());
	Translator.CreateDatabase();
	Translator.EvaluateAllRules();
	Translator.EvaluateAllQueries();
	return 0;
}
