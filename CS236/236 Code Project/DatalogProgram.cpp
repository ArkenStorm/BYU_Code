#include "DatalogProgram.h"
#include <iostream>

DatalogProgram::DatalogProgram() {}

void DatalogProgram::ToStr() {
	cout << "Success!" << endl;
	cout << "Schemes(" << schemeList.size() << "):" << endl;
	for (size_t i = 0; i < schemeList.size(); ++i) {
		cout << "  " << schemeList.at(i).ToStr() << endl;
	}
	cout << "Facts(" << factList.size() << "):" << endl;
	for (size_t i = 0; i < factList.size(); ++i) {
		cout << "  " << factList.at(i).ToStr() << "." << endl;
	}
	cout << "Rules(" << ruleList.size() << "):" << endl;
	for (size_t i = 0; i < ruleList.size(); ++i) {
		cout << "  " << ruleList.at(i).ToStr() << "." << endl;
	}
	cout << "Queries(" << queryList.size() << "):" << endl;
	for (size_t i = 0; i < queryList.size(); ++i) {
		cout << "  " << queryList.at(i).ToStr() << "?" << endl;
	}
	cout << "Domain(" << domainStrings.size() << "):" << endl;
	for (string const& builder : domainStrings) {
		cout << "  " << builder << endl;
	}
}

void DatalogProgram::AddScheme(Predicate& inScheme) {
	schemeList.push_back(inScheme);
}

void DatalogProgram::AddFact(Predicate& inFact) {
	factList.push_back(inFact);
}

void DatalogProgram::AddQuery(Predicate& inQuery) {
	queryList.push_back(inQuery);
}

void DatalogProgram::AddRule(Rule& inRule) {
	ruleList.push_back(inRule);
}

void DatalogProgram::InsertDomain(string inFactString) {
	domainStrings.insert(inFactString);
}

vector<Predicate> DatalogProgram::GetSchemes() {
	return schemeList;
}

vector<Predicate> DatalogProgram::GetFacts() {
	return factList;
}

vector<Predicate> DatalogProgram::GetQueries() {
	return queryList;
}

vector<Rule> DatalogProgram::GetRules() {
	return ruleList;
}
