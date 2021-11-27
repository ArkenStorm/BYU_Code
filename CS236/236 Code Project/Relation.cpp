#include "Relation.h"

Relation::Relation() {}

Relation::Relation(string inName, Scheme& inScheme) {
	name = inName;
	scheme = inScheme;
}

void Relation::AddTuple(Tuple& newTuple) {
	allTuples.insert(newTuple);
}

void Relation::ToString() {
	for (Tuple next : allTuples) {
		string spacer = "  ";
		for (size_t i = 0; i < scheme.size(); ++i) {
			cout << spacer << scheme[i] << "=" << next[i];
			spacer = ", ";
		}
		if (scheme.size() != 0) {
			cout << endl;
		}
	}
}

set<Tuple> Relation::GetTuples() {
	return allTuples;
}

Scheme Relation::GetScheme() {
	return scheme;
}

string Relation::GetName() {
	return name;
}

void Relation::ReceiveTuples(set<Tuple> inTuples) {
	this->allTuples = inTuples;
}

Relation Relation::SelectConstant(int index, string value) {
	Relation tempRelation(this->name, this->scheme);
	for (Tuple temp : allTuples) {
		if (temp.at(index) == value) {
			tempRelation.AddTuple(temp);
		}
	}
	return tempRelation;
}

Relation Relation::SelectTwoIndex(int firstIndex, int secondIndex) {
	Relation tempRelation(this->name, this->scheme);
	for (Tuple temp : allTuples) {
		if (temp.at(firstIndex) == temp.at(secondIndex)) {
			tempRelation.AddTuple(temp);
		}
	}
	return tempRelation;
}

Relation Relation::Project(vector<int> projectIndices) {
	Relation tempRelation(this->name, this->scheme);
	for (Tuple temp : allTuples) {
		Tuple tempTuple;
		for (size_t i = 0; i < projectIndices.size(); ++i) {
			tempTuple.push_back(temp.at(projectIndices.at(i)));
		}
		tempRelation.AddTuple(tempTuple);
	}
	return tempRelation;
}

Relation Relation::Rename(Scheme renameScheme) {
	Relation tempRelation(this->name, renameScheme);
	tempRelation.ReceiveTuples(this->allTuples);
	return tempRelation;
}

Relation Relation::Join(Relation& joinRelation) {
	Scheme tS1 = this->GetScheme();
	Scheme tS2 = joinRelation.GetScheme();
	Scheme returnedScheme = CombineSchemes(tS1, tS2);
	Relation newRelation(this->name, returnedScheme);
	for (Tuple t1 : this->allTuples) {
		for (Tuple t2 : joinRelation.GetTuples()) {
			if (IsJoinable(t1, t2, tS1, tS2)) {
				Tuple tempTuple = CombineTuples(t1, t2);
				newRelation.AddTuple(tempTuple);
			}
		}
	}
	return newRelation;
}

Relation Relation::Unite(Relation& inRelation) {
	Relation onlyNewTuples(this->name, this->scheme);
	for (Tuple t : inRelation.GetTuples()) {
		if (this->allTuples.insert(t).second) {
			onlyNewTuples.AddTuple(t);
		}
	}
	return onlyNewTuples;
}

Scheme Relation::CombineSchemes(Scheme& scheme1, Scheme& scheme2) {
	Scheme newScheme = scheme1;
	set<string> attributeSeen;
	for (size_t i = 0; i < scheme2.size(); ++i) {
		if (attributeSeen.find(scheme2.at(i)) == attributeSeen.end()) {
			newScheme.push_back(scheme2.at(i));
		}
	}
	return newScheme;
}

bool Relation::IsJoinable(Tuple& tuple1, Tuple& tuple2, Scheme& scheme1, Scheme& scheme2) {
	for (auto const [name1, attribute1] : ZipContainer<Scheme, Tuple>(scheme1, tuple1)) {
		for (auto const [name2, attribute2] : ZipContainer<Scheme, Tuple>(scheme2, tuple2)) {
			if (name1 == name2 && attribute1 != attribute2) {
				return false;
			}
		}
	}
	return true;
}

Tuple Relation::CombineTuples(Tuple& tuple1, Tuple& tuple2) {
	Tuple newTuple = tuple1;
	set<string> valueSeen;
	for (size_t i = 0; i < tuple2.size(); ++i) {
		if (valueSeen.find(tuple2.at(i)) == valueSeen.end()) {
			newTuple.push_back(tuple2.at(i));
		}
	}
	return newTuple;
}
