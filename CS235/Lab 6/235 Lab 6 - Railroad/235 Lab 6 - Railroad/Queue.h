#ifndef QUEUE_H
#define QUEUE_H

#include "Deque.h"

template<typename T>
class Queue
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
		internalDeque.pop_front();
	}

	T& top()
	{
		return internalDeque.frontValue();
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