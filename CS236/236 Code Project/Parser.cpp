#include <iostream>
#include <vector>
#include <set>
#include "Parser.h"
using namespace std;

Parser::Parser() {
	tokenList = vector<Token>();
	tokenIndex = 0;
}

Parser::Parser(vector<Token> listOfTokens) {
	tokenList = listOfTokens;
	tokenIndex = 0;
}

void Parser::Parse() {
	try {
		ParseDatalogProgram();
		if (tokenList.at(tokenIndex).GetTokenType() != ENDF) {
			throw tokenList.at(tokenIndex);
		}
	}
	catch (Token errorToken) {
		cout << "Failure!" << endl
			 << "  " << errorToken.ToStr() << endl;
		return;
	}
	//currentProgram.ToStr();
}

void Parser::ParseDatalogProgram() {
	Match(SCHEMES);
	Match(COLON);
	ParseScheme();
	currentProgram.AddScheme(currentPredicate);
	currentPredicate.resetParameters();
	ParseSchemeList();
	Match(FACTS);
	Match(COLON);
	ParseFactList();
	Match(RULES);
	Match(COLON);
	ParseRuleList();
	Match(QUERIES);
	Match(COLON);
	ParseQuery();
	currentProgram.AddQuery(currentPredicate);
	currentPredicate.resetParameters();
	ParseQueryList();
}

void Parser::ParseSchemeList() {
	if (tokenList.at(tokenIndex).GetTokenType() != ID) {
		return;
	}
	else if (tokenList.at(tokenIndex).GetTokenType() == ID) {
		ParseScheme();
		currentProgram.AddScheme(currentPredicate);
		currentPredicate.resetParameters();
		ParseSchemeList();
	}
}

void Parser::ParseFactList() {
	if (tokenList.at(tokenIndex).GetTokenType() != ID) {
		return;
	}
	else if (tokenList.at(tokenIndex).GetTokenType() == ID) {
		ParseFact();
		currentProgram.AddFact(currentPredicate);
		currentPredicate.resetParameters();
		ParseFactList();
	}
}

void Parser::ParseRuleList() {
	if (tokenList.at(tokenIndex).GetTokenType() != ID) {
		return;
	}
	else if (tokenList.at(tokenIndex).GetTokenType() == ID) {
		ParseRule();
		currentProgram.AddRule(currentRule);
		currentPredicate.resetParameters();
		currentRule.ResetPredicates();
		ParseRuleList();
	}
}

void Parser::ParseQueryList() {
	if (tokenList.at(tokenIndex).GetTokenType() != ID) {
		return;
	}
	else if (tokenList.at(tokenIndex).GetTokenType() == ID) {
		ParseQuery();
		currentProgram.AddQuery(currentPredicate);
		currentPredicate.resetParameters();
		ParseQueryList();
	}
}

void Parser::ParseScheme() {
	Match(ID);
	currentPredicate.SetValue(currentWorkingToken.GetTokenValue());
	Match(LEFT_PAREN);
	Match(ID);
	currentParam.SetTypeAndValue(currentWorkingToken.GetTokenValue(), ID);
	currentPredicate.AddParam(currentParam);
	ParseIDList();
	Match(RIGHT_PAREN);
}

void Parser::ParseFact() {
	Match(ID);
	currentPredicate.SetValue(currentWorkingToken.GetTokenValue());
	Match(LEFT_PAREN);
	Match(STRING);
	currentProgram.InsertDomain(currentWorkingToken.GetTokenValue());
	currentParam.SetTypeAndValue(currentWorkingToken.GetTokenValue(), STRING);
	currentPredicate.AddParam(currentParam);
	ParseStringList();
	Match(RIGHT_PAREN);
	Match(PERIOD);
}

void Parser::ParseRule() {
	ParseHeadPredicate();
	currentRule.SetHeadPredicate(currentPredicate);
	currentPredicate.resetParameters();
	Match(COLON_DASH);
	ParsePredicate();
	currentRule.AddPredicate(currentPredicate);
	currentPredicate.resetParameters();
	ParsePredicateList();
	Match(PERIOD);
}

void Parser::ParseQuery() {
	ParsePredicate();
	Match(Q_MARK);
}

void Parser::ParseHeadPredicate() {
	Match(ID);
	currentPredicate.SetValue(currentWorkingToken.GetTokenValue());
	Match(LEFT_PAREN);
	Match(ID);
	currentParam.SetTypeAndValue(currentWorkingToken.GetTokenValue(), ID);
	currentPredicate.AddParam(currentParam);
	ParseIDList();
	Match(RIGHT_PAREN);
}

void Parser::ParsePredicate() {
	Match(ID);
	currentPredicate.SetValue(currentWorkingToken.GetTokenValue());
	Match(LEFT_PAREN);
	ParseParameter();
	currentPredicate.AddParam(currentParam);
	ParseParameterList();
	Match(RIGHT_PAREN);
}

void Parser::ParsePredicateList() {
	if (tokenList.at(tokenIndex).GetTokenType() != COMMA) {
		return;
	}
	else {
		Match(COMMA);
		ParsePredicate();
		currentRule.AddPredicate(currentPredicate);
		currentPredicate.resetParameters();
		ParsePredicateList();
	}
}

void Parser::ParseParameterList() {
	if (tokenList.at(tokenIndex).GetTokenType() != COMMA) {
		return;
	}
	else {
		Match(COMMA);
		ParseParameter();
		tempString = "";
		currentPredicate.AddParam(currentParam);
		ParseParameterList();
	}
}

void Parser::ParseStringList() {
	if (tokenList.at(tokenIndex).GetTokenType() != COMMA) {
		return;
	}
	else {
		Match(COMMA);
		Match(STRING);
		currentProgram.InsertDomain(currentWorkingToken.GetTokenValue());
		currentParam.SetTypeAndValue(currentWorkingToken.GetTokenValue(), STRING);
		currentPredicate.AddParam(currentParam);
		ParseStringList();
	}
}

void Parser::ParseIDList() {
	if (tokenList.at(tokenIndex).GetTokenType() != COMMA) {
		return;
	}
	else {
		Match(COMMA);
		Match(ID);
		currentParam.SetTypeAndValue(currentWorkingToken.GetTokenValue(), ID);
		currentPredicate.AddParam(currentParam);
		ParseIDList();
	}
}

void Parser::ParseParameter() {
	if (tokenList.at(tokenIndex).GetTokenType() == STRING) {
		Match(STRING);
		currentParam.SetTypeAndValue(currentWorkingToken.GetTokenValue(), STRING);
	}
	else if (tokenList.at(tokenIndex).GetTokenType() == ID) {
		Match(ID);
		currentParam.SetTypeAndValue(currentWorkingToken.GetTokenValue(), ID);
	}
	else if (tokenList.at(tokenIndex).GetTokenType() == LEFT_PAREN) {
		expressionFinished = false;
		ParseExpression();
		if (expressionFinished) {
			currentParam.SetTypeAndValue(tempString);
		}
	}
	else {
		throw tokenList.at(tokenIndex);
	}
}

void Parser::ParseExpression() {
	expressionFinished = false;
	Match(LEFT_PAREN);
	tempString += currentWorkingToken.GetTokenValue();
	ParseParameter();
	if (!expressionFinished) {
		tempString += currentParam.GetValue();
	}
	ParseOperator();
	expressionFinished = false;
	tempString += currentParam.GetValue();
	ParseParameter();
	if (!expressionFinished) {
		tempString += currentParam.GetValue();
	}
	Match(RIGHT_PAREN);
	expressionFinished = true;
	tempString += currentWorkingToken.GetTokenValue();
}

void Parser::ParseOperator() {
	if (tokenList.at(tokenIndex).GetTokenType() == ADD) {
		Match(ADD);
		currentParam.SetTypeAndValue(currentWorkingToken.GetTokenValue());
	}
	else if (tokenList.at(tokenIndex).GetTokenType() == MULTIPLY) {
		Match(MULTIPLY);
		currentParam.SetTypeAndValue(currentWorkingToken.GetTokenValue());
	}
	else {
		throw tokenList.at(tokenIndex);
	}
}

void Parser::Match(TokenType currentToken) {
	currentWorkingToken = tokenList.at(tokenIndex);
	if (tokenList.at(tokenIndex).GetTokenType() != currentToken) {
		throw tokenList.at(tokenIndex);
	}
	else {
		++tokenIndex;
	}
}

DatalogProgram& Parser::GetProgram() {
	return currentProgram;
}
