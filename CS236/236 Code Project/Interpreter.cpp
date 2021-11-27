#include "Interpreter.h"

Interpreter::Interpreter(DatalogProgram& inProgram) {
	dlCode = inProgram;
}

void Interpreter::CreateDatabase() {
	CreateRelations();
	CreateTuples();
}

void Interpreter::CreateRelations() {
	Scheme tempScheme;
	for (size_t i = 0; i < dlCode.GetSchemes().size(); ++i) {
		for (size_t j = 0; j < dlCode.GetSchemes().at(i).GetParams().size(); ++j) {
			tempScheme.push_back(dlCode.GetSchemes().at(i).GetParams().at(j).ToStr());
		}
		Relation newRelation(dlCode.GetSchemes().at(i).GetID(), tempScheme);
		allData[dlCode.GetSchemes().at(i).GetID()] = newRelation;
		tempScheme.clear();
	}
}

void Interpreter::CreateTuples() {
	for (size_t i = 0; i < dlCode.GetFacts().size(); ++i) {
		Tuple tempTuple;
		for (size_t j = 0; j < dlCode.GetFacts().at(i).GetParams().size(); ++j) {
			tempTuple.push_back(dlCode.GetFacts().at(i).GetParams().at(j).ToStr());
		}
		string tempName = dlCode.GetFacts().at(i).GetID();
		allData.at(tempName).AddTuple(tempTuple);
	}
}

void Interpreter::EvaluateAllQueries() {
	cout << endl << "Query Evaluation" << endl;

	Relation tempRelation;
	for (size_t i = 0; i < dlCode.GetQueries().size(); ++i) {
		tempRelation = EvaluatePredicate(dlCode.GetQueries().at(i));
		cout << dlCode.GetQueries().at(i).ToStr() << "? ";
		if (tempRelation.GetTuples().size() != 0) {
			cout << "Yes(" << tempRelation.GetTuples().size() << ")" << endl;
			tempRelation.ToString();
		}
		else {
			cout << "No" << endl;
		}
	}
}

Relation Interpreter::EvaluatePredicate(Predicate& currentPredicate) {
	map<string, int> firstSeen;
	map<int, string> formatMap;
	Relation tempRelation;
	vector<int> projectIndices;
	Scheme tempScheme;
	if (allData.find(currentPredicate.GetID()) != allData.end()) {
		tempRelation = allData.at(currentPredicate.GetID());
		for (size_t j = 0; j < currentPredicate.GetParams().size(); ++j) {
			if (currentPredicate.GetParams().at(j).GetIsID()) {
				if (firstSeen.find(currentPredicate.GetParams().at(j).ToStr()) != firstSeen.end()) {
					tempRelation = tempRelation.SelectTwoIndex(firstSeen.at(currentPredicate.GetParams().at(j).ToStr()), j);
				}
				else {
					firstSeen[currentPredicate.GetParams().at(j).ToStr()] = j;
				}
			}
			else if (currentPredicate.GetParams().at(j).GetIsString()) {
				tempRelation = tempRelation.SelectConstant(j, currentPredicate.GetParams().at(j).GetValue());
			}
			else {
				//who knows, expressions?
			}
		}
		for (auto swapMap : firstSeen) {
			formatMap[swapMap.second] = swapMap.first;
		}
		for (auto builder : formatMap) {
			projectIndices.push_back(builder.first);
			tempScheme.push_back(builder.second);
		}
		tempRelation = tempRelation.Project(projectIndices);
		tempRelation = tempRelation.Rename(tempScheme);
	}
	else {
		cout << currentPredicate.ToStr() << " No" << endl;
	}

	return tempRelation;
}

void Interpreter::EvaluateAllRules() {
	vector<set<int>> SCC;
	vector<Rule> ruleList = dlCode.GetRules();
	Graph dependencyGraph(ruleList);
	dependencyGraph.CreateAdjacencies(ruleList);
	Graph reverseGraph(ruleList);
	reverseGraph.CreateReverseAdjacencies(dependencyGraph);
	dependencyGraph.DFSForestWithSCC(reverseGraph.DFSForest(), SCC);
	int numPasses = 0;

	size_t newTuples = 0;

	dependencyGraph.ToString();
	cout << "Rule Evaluation" << endl;

	for (const set<int>& componentSet : SCC) {
		vector<int> component;
		for (int number : componentSet) {
			component.push_back(number);
		}
		cout << "SCC: ";
		string formatter = "R";
		ostringstream oSS;
		for (size_t i = 0; i < component.size(); ++i) {
			oSS << formatter << component.at(i);
			formatter = ",R";
		}
		cout << oSS.str() << endl;

		bool selfDependent = false;
		if (component.size() == 1) {
			if (dependencyGraph.GetNode(component.at(0)).GetAdjacentNodes().count(component.at(0)) != 0) {
				selfDependent = true;
			}
		}
		if (component.size() == 1 && !selfDependent) {
			cout << dlCode.GetRules().at(component.at(0)).ToStr() << "." << endl;
			Relation tempRelation = EvaluateRule(dlCode.GetRules().at(component.at(0)));
			++numPasses;
			tempRelation.ToString();
		}
		else {
			do {
				//fixed point algorithm
				newTuples = 0;
				for (size_t i = 0; i < component.size(); ++i) {
					Rule currentRule = dlCode.GetRules().at(component.at(i));
					cout << currentRule.ToStr() << "." << endl;
					Relation tempRelation = EvaluateRule(currentRule);
					if (tempRelation.GetTuples().size() > newTuples) {
						newTuples = tempRelation.GetTuples().size();
					}
					tempRelation.ToString();
				}
				++numPasses;
			} while (newTuples != 0);
		}
		cout << numPasses << " passes: " << oSS.str() << endl;
	}

	//cout << endl << "Schemes populated after " << numPasses << " passes through the Rules." << endl << endl;
}

Relation Interpreter::EvaluateRule(Rule& currentRule) {
	Relation tempRelation = EvaluatePredicate(currentRule.GetAllPredicates().at(0));
	vector<int> projectIndices;
	if (currentRule.GetAllPredicates().size() > 1) {
		for (size_t i = 1; i < currentRule.GetAllPredicates().size(); ++i) {
			Relation returnedRelation = EvaluatePredicate(currentRule.GetAllPredicates().at(i));
			tempRelation = tempRelation.Join(returnedRelation);
		}
	}
	for (size_t i = 0; i < currentRule.GetHeadPredicate().GetParams().size(); ++i) {
		for (size_t j = 0; j < tempRelation.GetScheme().size(); ++j) {
			if (tempRelation.GetScheme().at(j) == currentRule.GetHeadPredicate().GetParams().at(i).ToStr()) {
				projectIndices.push_back(j);
				break;
			}
		}
	}
	tempRelation = tempRelation.Project(projectIndices);
	tempRelation = tempRelation.Rename(allData.at(currentRule.GetHeadPredicate().GetID()).GetScheme());
	tempRelation = allData.at(currentRule.GetHeadPredicate().GetID()).Unite(tempRelation);
	return tempRelation;
}
