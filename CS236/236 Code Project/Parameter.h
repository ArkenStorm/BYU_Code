#ifndef PARAMETER_H
#define PARAMETER_H
#include <string>
#include "TokenType.h"
using namespace std;

class Parameter {
public:
	Parameter();
	string ToStr();
	string GetValue();
	bool GetIsString(); //constant
	bool GetIsID();		//variable
	bool GetIsExpression();
	void SetTypeAndValue(string inValue, TokenType inType = ENDF);

private:
	string parameterValue;
	bool isString;
	bool isID;
	bool isExpression;
};

#endif
