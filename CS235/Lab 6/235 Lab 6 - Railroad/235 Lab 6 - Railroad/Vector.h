#ifndef VECTOR_H
#define VECTOR_H

#include "Deque.h"

template<typename T>
class Vector
{
private:
	Deque<T> internalDeque;
public:
	void push_back(const T& item)
	{
		internalDeque.push_back(item);
	}

	void pop_back()
	{
		internalDeque.pop_back();
	}

	T& back()
	{
		return internalDeque.back();
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