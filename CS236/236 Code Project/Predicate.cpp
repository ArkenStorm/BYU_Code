#include "Predicate.h"
#include <sstream>

Predicate::Predicate(string inID) {
	stringID = inID;
}

void Predicate::AddParam(Parameter inParam) {
	allParameters.push_back(inParam);
}

string Predicate::ToStr() {
	ostringstream oSS;
	oSS << stringID << "(";
	for (size_t i = 0; i < allParameters.size() - 1; ++i) {
		oSS << allParameters.at(i).ToStr() << ",";
	}
	oSS << allParameters.at(allParameters.size() - 1).ToStr() << ")";

	return oSS.str();
}

void Predicate::SetValue(string inValue) {
	stringID = inValue;
}

void Predicate::resetParameters() {
	allParameters.clear();
}

string Predicate::GetID() {
	return stringID;
}

vector<Parameter> Predicate::GetParams() {
	return allParameters;
}
