#ifndef LEXER_H
#define LEXER_H
#include <vector>
#include <string>
#include <sstream>
#include <fstream>
#include "Token.h"
using namespace std;

class Lexer {
public:
	Lexer();
	Lexer(const char* inputFile);
	void ScanTokens();
	string PrintAllTokens();
	vector<Token> GetTokenList();

private:
	vector<Token> listOfTokens;
	int currentLine;
	ifstream inFS;
};

#endif
