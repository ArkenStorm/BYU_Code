#include <iostream>
#include <string>
#include <sstream>

#ifndef MYARRAY_H
#define MYARRAY_H

template<typename T>
class MyArray
{
private:
	size_t arraySize;
	T* newArray;
public:
	MyArray(const size_t maxSize) : arraySize(0)
	{
		newArray = (T*)malloc(maxSize * sizeof(T));
	}

	~MyArray()
	{
		free (newArray);
	}

	void push_back(T item)
	{
		newArray[arraySize++] = item;
	}

	size_t size()
	{
		return arraySize;
	}

	T& operator[](size_t i) const							// index access
	{
		return newArray[i];
	}

	class Iterator
	{
	private:
		size_t iterIndex;
		size_t filledIndices;
		T* newArray;
	public:
		Iterator(T* array, size_t size, size_t index) 
		{
			newArray = array;
			filledIndices = size;
			iterIndex = index;

		}
		bool operator!= (const Iterator& other) const   // not-equal
		{
			if (iterIndex == other.iterIndex)
			{
				return false;
			}

			else
			{
				return true;
			}
		}
		Iterator& operator++ ()                        // pre-increment ++
		{
			iterIndex++;
			return *this;
		}

		Iterator& operator++ (int)					  // post-increment ++
		{
			Iterator temp(*this);
			iterIndex++;
			return temp;
		}

		T& operator[](size_t i) const							// index access
		{
			return newArray[i];
		}

		T& operator*() const                          // dereference
		{
			return newArray[iterIndex];
		} 

		std::string toString() const 
		{
			std::ostringstream iterOSS;
			iterOSS << "size = " << filledIndices;
			return iterOSS.str();
		}

		friend std::ostream& operator<< (std::ostream& os, const Iterator& iter) 
		{
			os << iter.toString();
			return os;
		}
	};
	Iterator begin()			// pointer to first element
	{ 
		Iterator begin(newArray, arraySize, 0);
		return begin;
	}             
	Iterator end()                // pointer AFTER last element
	{
		Iterator end(newArray, arraySize, arraySize);
		return end;
	}

	std::string toString() const
	{
		std::ostringstream myArrayOSS;

		for (size_t i = 0; i < arraySize; i++)
		{
			myArrayOSS << newArray[i] << " ";
			if ((i + 1) % 10 == 0 && i > 1 && i < arraySize - 1)						// makes every line have 10 elements
			{
				myArrayOSS << std::endl;
			}
		}

		return myArrayOSS.str();
	}

	friend std::ostream& operator<< (std::ostream& os, const MyArray<T>& myArray) 
	{
		os << myArray.toString();
		return os;
	};
};
#endif // MYARRAY_H
