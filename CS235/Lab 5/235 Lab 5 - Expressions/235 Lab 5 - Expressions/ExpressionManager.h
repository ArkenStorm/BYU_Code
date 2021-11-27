#ifndef EXPRESSIONMANAGER_H
#define EXPRESSIONMANAGER_H

#include "ExpressionManagerInterface.h"

class ExpressionManager : public ExpressionManagerInterface
{
public:
	ExpressionManager();
	~ExpressionManager();
	virtual int value();
	virtual string infix();
	virtual string postfix();
	virtual string prefix();
	virtual string toString() const;
	void setExpression(string inputExpression);

private:
	string currentExpression;
};

#endif