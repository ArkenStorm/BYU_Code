#ifndef PREDICATE_H
#define PREDICATE_H
#include "Parameter.h"
#include <vector>

class Predicate {
public:
	Predicate(string inID = "");
	void AddParam(Parameter inParam);
	string ToStr();
	void SetValue(string inValue);
	void resetParameters();
	string GetID();
	vector<Parameter> GetParams();

private:
	string stringID;
	vector<Parameter> allParameters;
};
#endif
