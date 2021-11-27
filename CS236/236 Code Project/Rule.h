#ifndef RULE_H
#define RULE_H
#include "Predicate.h"

class Rule {
public:
	Rule();
	string ToStr();
	void SetHeadPredicate(Predicate inPredicate);
	Predicate GetHeadPredicate();
	vector<Predicate> GetAllPredicates();
	void AddPredicate(Predicate inPredicate);
	void ResetPredicates();

private:
	Predicate headPredicate;
	vector<Predicate> allPredicates;
};

#endif
