#ifndef DATALOG_PROGRAM_H
#define DATALOG_PROGRAM_H
#include "Predicate.h"
#include "Rule.h"
#include "Parameter.h"
#include <vector>
#include <string>
#include <set>

class DatalogProgram {
public:
	DatalogProgram();
	void ToStr();
	void AddScheme(Predicate& inScheme);
	void AddFact(Predicate& inFact);
	void AddQuery(Predicate& inQuery);
	void AddRule(Rule& inRule);
	void InsertDomain(string inFactString);
	vector<Predicate> GetSchemes();
	vector<Predicate> GetFacts();
	vector<Predicate> GetQueries();
	vector<Rule> GetRules();

private:
	vector<Predicate> schemeList;
	vector<Predicate> factList;
	vector<Predicate> queryList;
	vector<Rule> ruleList;
	set<string> domainStrings;
};
#endif
