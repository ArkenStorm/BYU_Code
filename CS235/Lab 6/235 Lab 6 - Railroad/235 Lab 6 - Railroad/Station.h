#ifndef STATION_H
#define STATION_H

#include "Deque.h"
#include "Queue.h"
#include "Stack.h"
#include "Vector.h"

template<typename T>
class Station
{
private:
	Vector<T> outgoingTrains;
	Stack<T> stackRoundhouse;
	Queue<T> queueRoundhouse;
	T turnTable;
	bool emptyTable;
public:
	Station()
	{
		emptyTable = true;
	}

	string addCar(const T& item)
	{
		if (emptyTable == false)
		{
			return "Turntable occupied!";
		}

		turnTable = item;
		emptyTable = false;

		return "OK";
	}

	string removeCar()
	{
		if (emptyTable == true)
		{
			return "Turntable empty!";
		}

		outgoingTrains.push_back(turnTable);
		turnTable = T();
		emptyTable = true;

		return "OK";
	}

	string topCar()
	{
		ostringstream turnTableSS;

		if (emptyTable == true)
		{
			return "Turntable empty!";
		}

		turnTableSS << turnTable;

		return turnTableSS.str();
	}

	string addStack()
	{
		if (emptyTable == true)
		{
			return "Turntable empty!";
		}

		stackRoundhouse.push(turnTable);
		turnTable = T();
		emptyTable = true;

		return "OK";
	}

	string removeStack()
	{
		if (stackRoundhouse.size() == 0)
		{
			return "Stack empty!";
		}

		if (emptyTable == false)
		{
			return "Turntable occupied!";
		}

		turnTable = stackRoundhouse.top();
		stackRoundhouse.pop();
		emptyTable = false;

		return "OK";
	}

	string topStack()
	{
		ostringstream stackSS;

		if (stackRoundhouse.size() == 0)
		{
			return "Stack empty!";
		}
		
		stackSS << stackRoundhouse.top();

		return stackSS.str();
	}

	size_t sizeStack()
	{
		return stackRoundhouse.size();
	}

	string addQueue()
	{
		if (emptyTable == true)
		{
			return "Turntable empty!";
		}

		queueRoundhouse.push(turnTable);
		turnTable = T();
		emptyTable = true;

		return "OK";
	}

	string removeQueue()
	{
		if (queueRoundhouse.size() == 0)
		{
			return "Queue empty!";
		}

		if (emptyTable == false)
		{;
		return "Turntable occupied!";
		}

		turnTable = queueRoundhouse.top();
		queueRoundhouse.pop();
		emptyTable = false;

		return "OK";
	}

	string topQueue()
	{
		ostringstream queueSS;

		if (queueRoundhouse.size() == 0)
		{
			return "Queue empty!";
		}

		queueSS << queueRoundhouse.top();

		return queueSS.str();
	}

	size_t sizeQueue()
	{
		return queueRoundhouse.size();
	}

	string find(size_t train)
	{
		ostringstream locationSS;

		bool trainFound = false;

		if (turnTable == train)
		{
			trainFound = true;
			locationSS << "Turntable";
		}

		if (trainFound == false)
		{
			for (size_t i = 0; i < outgoingTrains.size(); ++i)
			{
				if (outgoingTrains.at(i) == train)
				{
					trainFound = true;
					locationSS << "Train[" << i << "]";
				}
			}
		}

		if (trainFound == false)
		{
			for (size_t i = 0; i < stackRoundhouse.size(); ++i)
			{
				if (stackRoundhouse.at(i) == train)
				{
					trainFound = true;
					locationSS << "Stack[" << i << "]";
				}
			}
		}

		if (trainFound == false)
		{
			for (size_t i = 0; i < queueRoundhouse.size(); ++i)
			{
				if (queueRoundhouse.at(i) == train)
				{
					trainFound = true;
					locationSS << "Queue[" << i << "]";
				}
			}
		}

		if (trainFound == false)
		{
			return "Not Found!";
		}

		return locationSS.str();
	}

	string toString() const
	{
		return outgoingTrains.toString();
	}
};

#endif