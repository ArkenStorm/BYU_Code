#ifndef DEQUE_H
#define DEQUE_H

#include "DequeInterface.h"

template<typename T>
class Deque : public DequeInterface<T>
{
public:
	static const size_t DEFAULT_CAPACITY = 4;

	Deque()
	{
		currentCapacity = DEFAULT_CAPACITY;
		numItems = 0;
		frontIndex = 0;
		rearIndex = 0;
		internalArray = new T[DEFAULT_CAPACITY];
	}

	~Deque()
	{
		delete[] internalArray;
	}

	/** Insert item at front of deque */
	virtual void push_front(const T& value)
	{
		if (numItems == currentCapacity) { reallocate(); }
		if (numItems != 0) { frontIndex = (frontIndex - 1) % currentCapacity; }

		internalArray[frontIndex] = value;
		++numItems;
	}

	/** Insert item at rear of deque */
	virtual void push_back(const T& value)
	{
		if (numItems == currentCapacity) { reallocate(); }
		if (numItems != 0) { rearIndex = (rearIndex + 1) % currentCapacity; }

		internalArray[rearIndex] = value;
		++numItems;
	}

	/** Remove the front item of the deque */
	virtual void pop_front()
	{
		internalArray[frontIndex] = 0;
		if (numItems != 1) { frontIndex = (frontIndex + 1) % currentCapacity; }
		numItems--;
	}

	/** Remove the rear item of the deque */
	virtual void pop_back()
	{
		internalArray[rearIndex] = 0;
		if (numItems != 1) { rearIndex = (rearIndex - 1) % currentCapacity; }
		numItems--;
	}

	/** Return the front item of the deque (Do not remove) */
	virtual T& front()
	{
		return internalArray[frontIndex];
	}

	/** Return the rear item of the deque (Do not remove) */
	virtual T& back()
	{
		return internalArray[rearIndex];
	}

	/** Return the number of items in the deque */
	virtual size_t size() const
	{
		return numItems;
	}

	/** Return true if deque is empty */
	virtual bool empty() const
	{
		if (numItems == 0) { return true; }

		return false;
	}

	/** Return item in deque at index (0 based) */
	virtual T& at(size_t index)
	{
		return internalArray[(frontIndex + index) % currentCapacity];
	}

	/** Return the deque items */
	virtual string toString() const
	{
		ostringstream outSS;

		for (size_t i = frontIndex; i < numItems; ++i)
		{
			outSS << internalArray[i % currentCapacity] << " ";
		}

		return outSS.str();
	}

	void reallocate()
	{
		T* tempArray = new T[currentCapacity * 2];
		for (size_t i = 0; i < numItems; i++)
		{
			tempArray[i] = internalArray[frontIndex];
			frontIndex = (frontIndex + 1) % currentCapacity;

			cout << tempArray[i] << " ";
		}
		cout << endl;
		
		delete[] internalArray;
		internalArray = tempArray;
		frontIndex = 0;
		rearIndex = numItems - 1;
		currentCapacity *= 2;
	}

	T& frontValue()
	{
		return internalArray[frontIndex];
	}

	T& rearValue()
	{
		return internalArray[rearIndex];
	}

private:
	size_t currentCapacity;
	size_t numItems;
	size_t frontIndex;
	size_t rearIndex;
	T* internalArray;
};

#endif
