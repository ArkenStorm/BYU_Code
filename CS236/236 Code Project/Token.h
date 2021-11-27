#ifndef TOKEN_H
#define TOKEN_H
#include <string>
#include <sstream>
#include "TokenType.h"
using namespace std;

class Token {
public:
	Token(TokenType scanType = UNDEFINED, string scanValue = "", int scanLine = -1);
	string ToStr();
	string enumConverter(TokenType scanType);
	TokenType GetTokenType();
	string GetTokenValue();

private:
	TokenType type;
	string value;
	int line;
};

#endif
