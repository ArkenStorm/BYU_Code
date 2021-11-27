#include "Parameter.h"

Parameter::Parameter() {
	parameterValue = "";
	isString = false;
	isID = false;
	isExpression = false;
}

string Parameter::ToStr() {
	return parameterValue;
}

string Parameter::GetValue() {
	return parameterValue;
}

bool Parameter::GetIsString() {
	return isString;
}

bool Parameter::GetIsID() {
	return isID;
}

bool Parameter::GetIsExpression() {
	return isExpression;
}

void Parameter::SetTypeAndValue(string inValue, TokenType inType) {
	parameterValue = inValue;
	isString = false;
	isID = false;
	isExpression = false;
	switch (inType) {
	case STRING:
		isString = true;
		break;
	case ID:
		isID = true;
		break;
	default:
		isExpression = true;
		break;
	}
}
