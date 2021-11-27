#include "Token.h"

Token::Token(TokenType scanType, string scanValue, int scanLine) {
	type = scanType;
	value = scanValue;
	line = scanLine;
}

string Token::ToStr() {
	ostringstream oss;

	oss << "(" << enumConverter(type) << ",\"" << value << "\"," << line << ")";
	return oss.str();
}

string Token::enumConverter(TokenType scanType) {
	switch (scanType) {
	case COMMA:
		return "COMMA";
		break;
	case PERIOD:
		return "PERIOD";
		break;
	case Q_MARK:
		return "Q_MARK";
		break;
	case LEFT_PAREN:
		return "LEFT_PAREN";
		break;
	case RIGHT_PAREN:
		return "RIGHT_PAREN";
		break;
	case COLON:
		return "COLON";
		break;
	case COLON_DASH:
		return "COLON_DASH";
		break;
	case MULTIPLY:
		return "MULTIPLY";
		break;
	case ADD:
		return "ADD";
		break;
	case SCHEMES:
		return "SCHEMES";
		break;
	case FACTS:
		return "FACTS";
		break;
	case RULES:
		return "RULES";
		break;
	case QUERIES:
		return "QUERIES";
		break;
	case ID:
		return "ID";
		break;
	case STRING:
		return "STRING";
		break;
	case COMMENT:
		return "COMMENT";
		break;
	case UNDEFINED:
		return "UNDEFINED";
		break;
	case ENDF:
		return "EOF";
		break;
	}
	return "ERROR";
}

TokenType Token::GetTokenType() {
	return type;
}

string Token::GetTokenValue() {
	return value;
}
