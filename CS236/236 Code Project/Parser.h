#ifndef PARSER_H
#define PARSER_H
#include "Token.h"
#include "DatalogProgram.h"

class Parser {
public:
	Parser();
	Parser(vector<Token> listOfTokens);
	void Parse();
	void ParseDatalogProgram();
	void ParseSchemeList();
	void ParseFactList();
	void ParseRuleList();
	void ParseQueryList();
	void ParseScheme();
	void ParseFact();
	void ParseRule();
	void ParseQuery();
	void ParseHeadPredicate();
	void ParsePredicate();
	void ParsePredicateList();
	void ParseParameterList();
	void ParseStringList();
	void ParseIDList();
	void ParseParameter();
	void ParseExpression();
	void ParseOperator();
	void Match(TokenType currentToken);
	DatalogProgram& GetProgram();

private:
	vector<Token> tokenList;
	int tokenIndex;
	DatalogProgram currentProgram;
	Token currentWorkingToken;
	Parameter currentParam;
	Predicate currentPredicate;
	string tempString;
	Rule currentRule;
	bool expressionFinished = false;
};

#endif
