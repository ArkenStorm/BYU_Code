#ifndef RELATION_H
#define RELATION_H
#include <iostream>
#include <set>
#include <map>
#include "Tuple.h"
#include "Scheme.h"
#include "ZipContainer.h"

class Relation {
public:
	Relation();
	Relation(string inName, Scheme& inScheme);
	void AddTuple(Tuple& newTuple);
	void ToString();
	set<Tuple> GetTuples();
	Scheme GetScheme();
	string GetName();
	void ReceiveTuples(set<Tuple> inTuples);
	Relation SelectConstant(int index, string value);
	Relation SelectTwoIndex(int firstIndex, int secondIndex);
	Relation Project(vector<int> projectIndices);
	Relation Rename(Scheme renameScheme);
	Relation Join(Relation& joinRelation);
	Relation Unite(Relation& inRelation);

private:
	string name;
	Scheme scheme;
	set<Tuple> allTuples;
	Scheme CombineSchemes(Scheme& scheme1, Scheme& scheme2);
	bool IsJoinable(Tuple& tuple1, Tuple& tuple2, Scheme& scheme1, Scheme& scheme2);
	Tuple CombineTuples(Tuple& tuple1, Tuple& tuple2);
};
#endif
