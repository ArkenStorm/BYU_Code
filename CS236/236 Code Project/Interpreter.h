#ifndef INTERPRETER_H
#define INTERPRETER_H
#include "DatalogProgram.h"
#include "Database.h"
#include "Graph.h"
#include <map>
#include <sstream>

class Interpreter {
public:
	Interpreter(DatalogProgram& inProgram);
	void CreateDatabase();
	void CreateRelations();
	void CreateTuples();
	void EvaluateAllQueries();
	Relation EvaluatePredicate(Predicate& currentPredicate);
	void EvaluateAllRules();
	Relation EvaluateRule(Rule& currentRule);

private:
	DatalogProgram dlCode;
	Database allData;
};
#endif
