#include <iostream>
#include <string>
#include <sstream>
#include <stack>
#include <cctype>
#include "ExpressionManager.h"
#include "ExpressionManagerInterface.h"
using std::string;
using std::istringstream;
using std::stack;
using std::ostringstream;

ExpressionManager::ExpressionManager() {}

ExpressionManager::~ExpressionManager() {}

int ExpressionManager::value(void)
{
	istringstream inSS(currentExpression);
	istringstream copySS;
	stack<string> operatorStack;
	stack<int> integerStack;
	string currentItem;
	int currentInt = 0;
	int leftOperand = 0;
	int rightOperand = 0;
	int newValue = 0;

	while (inSS >> currentItem)
	{
		if (isdigit(currentItem.at(0)))
		{
			copySS.clear();
			copySS.str(currentItem);
			copySS >> currentInt;
			integerStack.push(currentInt);
		}

		else
		{
			rightOperand = integerStack.top();
			integerStack.pop();
			leftOperand = integerStack.top();
			integerStack.pop();

			if (currentItem == "+")
			{
				newValue = leftOperand + rightOperand;
				integerStack.push(newValue);
			}

			if (currentItem == "-")
			{
				newValue = leftOperand - rightOperand;
				integerStack.push(newValue);
			}

			if (currentItem == "*")
			{
				newValue = leftOperand * rightOperand;
				integerStack.push(newValue);
			}

			if (currentItem == "/")
			{
				newValue = leftOperand / rightOperand;
				integerStack.push(newValue);
			}

			if (currentItem == "%")
			{
				newValue = leftOperand % rightOperand;
				integerStack.push(newValue);
			}

		}
	}

	if (integerStack.size() > 0)
	{
		newValue = integerStack.top();
	}

	return newValue;
}

string ExpressionManager::infix(void)
{
	istringstream inSS(currentExpression);
	stack<string> operatorStack;
	stack<int> integerStack;
	string currentItem;
	int currentInt = 0;
	int operatorCount = 0;

	while (!inSS.eof())
	{
		inSS >> currentItem;
		if (!isdigit(currentItem.at(0)))
		{
			try
			{
				if (currentItem != "+" && currentItem != "-" && currentItem != "/" && currentItem != "*" && currentItem != "%"
					&& currentItem != "(" && currentItem != ")" && currentItem != "[" && currentItem != "]" && currentItem != "{" && currentItem != "}")
				{
					throw currentItem;
				}

				if (currentItem != "(" && currentItem != ")" && currentItem != "[" && currentItem != "]" && currentItem != "{" && currentItem != "}" && integerStack.size() == 0)
				{
					throw integerStack;
				}
			}
			catch (string invalidOperator)
			{
				return "Caught exception: NOT Infix: Illegal Operator";
			}
			catch (stack<int> emptyStack)
			{
				return "Caught exception: NOT Infix: Missing Operands";
			}

			operatorStack.push(currentItem);
			if (currentItem != "(" && currentItem != ")" && currentItem != "[" && currentItem != "]" && currentItem != "{" && currentItem != "}")
			{
				operatorCount++;
			}
		}

		else
		{
			istringstream copySS(currentItem);
			copySS >> currentInt;
			integerStack.push(currentInt);
			continue;
		}

		try
		{
			if (currentItem == ")")
			{
				while (operatorStack.size() != 0 && operatorStack.top() != "(")
				{
					operatorStack.pop();
				}

				if (operatorStack.size() == 0)
				{
					throw currentItem;
				}

				else
				{
					operatorStack.pop();
					continue;
				}
			}

			else if (currentItem == "]")
			{
				while (operatorStack.size() != 0 && operatorStack.top() != "[")
				{
					operatorStack.pop();
				}

				if (operatorStack.size() == 0)
				{
					throw currentItem;
				}

				else
				{
					operatorStack.pop();
					continue;
				}
			}

			else if (currentItem == "}")
			{
				while (operatorStack.size() != 0 && operatorStack.top() != "{")
				{
					operatorStack.pop();
				}

				if (operatorStack.size() == 0)
				{
					throw currentItem;
				}

				else
				{
					operatorStack.pop();
					continue;
				}
			}
		}
		catch (string wrongParen)
		{
			return "Caught exception: NOT Infix: Paren Mis-match";
		}
	}

	/*
	//put all the pushes and pops before this
	try
	{
		if (operatorStack.size() > 0)
		{
			throw operatorStack;
		}
	}
	catch (stack<string> nonEmptyStack)
	{
		return "Caught exception: NOT Infix: Unbalanced";
	}

	try
	{
	if (!(operatorCount == integerStack.size() - 1))
	{
	throw operatorCount;
	}
	}
	catch (int insufficientOperators)
	{
	return "Caught exception: NOT Infix: Missing Operators";
	}
	*/

	return currentExpression;
}

string ExpressionManager::postfix(void)
{
	istringstream inSS(currentExpression);
	ostringstream outSS;
	string postfixExpression;
	stack<string> operatorStack;
	string currentItem;
	int currentPrecedence = 0;
	int stackPrecedence = 0;

	outSS.clear();

	while (inSS >> currentItem)
	{
		if (!isdigit(currentItem.at(0)))
		{
			//sets the current precedence of the operator
			if (currentItem == ")" || currentItem == "]" || currentItem == "}")
			{
				currentPrecedence = 3;
			}

			else if (currentItem == "*" || currentItem == "/" || currentItem == "%")
			{
				currentPrecedence = 2;
			}

			else if (currentItem == "+" || currentItem == "-")
			{
				currentPrecedence = 1;
			}

			else if (currentItem == "(" || currentItem == "[" || currentItem == "{")
			{
				currentPrecedence = 0;
			}

			if (operatorStack.size() == 0)
			{
				stackPrecedence = currentPrecedence;
				operatorStack.push(currentItem);
				continue;
			}

			//compares current and stack precedences
			if (currentPrecedence == 0)
			{
				operatorStack.push(currentItem);
				stackPrecedence = currentPrecedence;
				continue;
			}

			if (currentPrecedence == 3)
			{
				if (currentItem == ")")
				{
					while (operatorStack.size() != 0 && operatorStack.top() != "(")
					{
						if (operatorStack.top() != "(" && operatorStack.top() != "[" && operatorStack.top() != "{")
						{
							outSS << operatorStack.top() << " ";
						}
						operatorStack.pop();
					}
					operatorStack.pop();
					continue;
				}

				else if (currentItem == "]")
				{
					while (operatorStack.size() != 0 && operatorStack.top() != "[")
					{
						if (operatorStack.top() != "(" && operatorStack.top() != "[" && operatorStack.top() != "{")
						{
							outSS << operatorStack.top() << " ";
						}
						operatorStack.pop();
					}
					operatorStack.pop();
					continue;
				}

				else if (currentItem == "}")
				{
					while (operatorStack.size() != 0 && operatorStack.top() != "{")
					{
						if (operatorStack.top() != "(" && operatorStack.top() != "[" && operatorStack.top() != "{")
						{
							outSS << operatorStack.top() << " ";
						}
						operatorStack.pop();
					}
						
					operatorStack.pop();
					continue;				
				}
				continue;
			}

			if (stackPrecedence < currentPrecedence)
			{
				operatorStack.push(currentItem);
			}

			else
			{
				while (stackPrecedence >= currentPrecedence && !operatorStack.empty() && operatorStack.top() != "(" && operatorStack.top() != "[" && operatorStack.top() != "{")
				{
					if (operatorStack.top() != "(" && operatorStack.top() != "[" && operatorStack.top() != "{")
					{
						outSS << operatorStack.top() << " ";
					}
					operatorStack.pop();
					stackPrecedence -= 1;
				}
				operatorStack.push(currentItem);
			}
			
			stackPrecedence = currentPrecedence;
		}

		else
		{
			outSS << currentItem << " ";
		}
	}

	if (inSS.eof() == true && operatorStack.size() > 0)
	{
		while (operatorStack.size() > 0)
		{
			if (operatorStack.top() != "(" && operatorStack.top() != "[" && operatorStack.top() != "{")
			{
				outSS << operatorStack.top() << " ";
			}
			operatorStack.pop();
		}
	}

	postfixExpression = outSS.str();
	currentExpression = postfixExpression;

	return postfixExpression;
}

string ExpressionManager::prefix(void)
{
	return "NOT IMPLEMENTED";
}

string ExpressionManager::toString() const
{
	return "reer";
}

void ExpressionManager::setExpression(string inputExpression)
{
	currentExpression = inputExpression;
}