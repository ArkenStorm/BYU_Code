#include "Rule.h"
#include <sstream>

Rule::Rule() {}

string Rule::ToStr() {
	ostringstream oSS;
	oSS << headPredicate.ToStr() << " :- ";
	for (size_t i = 0; i < allPredicates.size() - 1; ++i) {
		oSS << allPredicates.at(i).ToStr() << ",";
	}
	oSS << allPredicates.at(allPredicates.size() - 1).ToStr();
	return oSS.str();
}

void Rule::SetHeadPredicate(Predicate inPredicate) {
	headPredicate = inPredicate;
}

Predicate Rule::GetHeadPredicate() {
	return headPredicate;
}

vector<Predicate> Rule::GetAllPredicates() {
	return allPredicates;
}

void Rule::AddPredicate(Predicate inPredicate) {
	allPredicates.push_back(inPredicate);
}

void Rule::ResetPredicates() {
	allPredicates.clear();
}
