#ifndef STACK_H
#define STACK_H

#include "Deque.h"

template<typename T>
class Stack
{
private:
	Deque<T> internalDeque;
public:
	void push(const T& item)
	{
		internalDeque.push_back(item);
	}

	void pop()
	{
		internalDeque.pop_back();
	}

	T& top()
	{
		return internalDeque.rearValue();
	}

	size_t size()
	{
		return internalDeque.size();
	}

	T& at(size_t index)
	{
		return internalDeque.at(index);
	}

	string toString() const
	{
		return internalDeque.toString();
	}
};

#endif