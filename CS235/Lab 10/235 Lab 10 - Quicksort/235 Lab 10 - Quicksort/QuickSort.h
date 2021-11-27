#ifndef QUICKSORT_H
#define QUICKSORT_H
#include <iostream>
#include <string>
#include <sstream>
#include "QSInterface.h"
using namespace std;

template<typename T>
class QuickSort : public QSInterface<T>
{
private:
	size_t sortComparisons;
	size_t sortExchanges;
	size_t currentSize;
	size_t currentCapacity;
	T* arrayData;

public:
	QuickSort()
	{
		arrayData = NULL;
		currentSize = 0;
		currentCapacity = 0;
	}
	
	~QuickSort()
	{
		delete[] arrayData;
	}

	/** Dynamically allocate an initial array to the QuickSort class. */
	virtual bool createArray(size_t capacity)
	{
		if (capacity <= 0)
		{
			return false;
		}

		if (arrayData == NULL)
		{
			arrayData = new T[capacity];
			currentCapacity = capacity;
			return true;
		}


		else
		{
			clear();
			delete[] arrayData;
			arrayData = new T[capacity];
			currentCapacity = capacity;
			return true;
		}

	}

	/** Add an element to the QuickSort array. Dynamically grow array as needed. */
	virtual bool addElement(T element)
	{
		if (currentSize == currentCapacity)
		{
			T* tempArray = new T[currentCapacity * 2];
			for (size_t i = 0; i < currentSize; i++)
			{
				tempArray[i] = arrayData[i];
			}

			delete[] arrayData;
			arrayData = tempArray;
			currentCapacity *= 2;
			//delete[] tempArray;
		}

		arrayData[currentSize] = element;
		currentSize += 1;

		return true;
	}

	/** Sort the elements of a QuickSort subarray using median and partition functions. */
	virtual bool sort(size_t left, size_t right)
	{
		sortComparisons = 0;
		sortExchanges = 0;

		int test = partition(left, right, medianOfThree(left, right));

		if (test == -1)
		{
			return false;
		}
		return true;
	}

	/** Sort all elements of the QuickSort array using median and partition functions. */
	virtual bool sortAll()
	{
		sortComparisons = 0;
		sortExchanges = 0;

		int test = partition(0, currentSize - 1, medianOfThree(0, currentSize - 1));

		if (test == -1)
		{
			return false;
		}
		return true;
	}

	/** Removes all items from the QuickSort array. */
	virtual bool clear()
	{
		for (size_t i = 0; i < currentSize; i++)
		{
			arrayData[i] = T();
		}
		//delete arrayData;
		currentSize = 0;
		return true;
	}

	/** Return size of the QuickSort array. */
	virtual size_t capacity() const
	{
		return currentCapacity;
	}

	/** Return number of elements in the QuickSort array. */
	virtual size_t size() const
	{
		return currentSize;
	}

	/** The median of three pivot selection has two parts:
	1) Calculates the middle index by averaging the given left and right indices:
	middle = (left + right)/2
	2) Then bubble-sorts the values at the left, middle, and right indices.

	After this method is called, data[left] <= data[middle] <= data[right].

	@param left - the left boundary for the subarray from which to find a pivot
	@param right - the right + 1 boundary for the subarray from which to find a pivot
	@return the index of the pivot (middle index).
	Return -1 if	1) the array is empty,
	2) if either of the given integers is out of bounds,
	3) or if the left index is not less than the right index.
	*/
	virtual int medianOfThree(size_t left, size_t right)
	{
		if (left > right || right == left + 1 || right == left)
		{
			return -1;
		}

		else if (left >= currentSize || right > currentSize || left < 0 || right < 0)
		{
			return -1;
		}

		size_t middle = (left + right) / 2;

		if (arrayData[middle] > arrayData[right])
		{
			T temp = arrayData[middle];
			arrayData[middle] = arrayData[right];
			arrayData[right] = temp;
			sortExchanges += 1;
		}
		sortComparisons += 1;

		if (arrayData[left] > arrayData[middle])
		{
			T temp = arrayData[middle];
			arrayData[middle] = arrayData[left];
			arrayData[left] = temp;
			sortExchanges += 1;
		}
		sortComparisons += 1;

		if (arrayData[middle] > arrayData[right])
		{
			T temp = arrayData[middle];
			arrayData[middle] = arrayData[right];
			arrayData[right] = temp;
			sortExchanges += 1;
		}
		sortComparisons += 1;

		return middle;
	}

	/** Partitions a subarray around a pivot value selected according
	to median-of-three pivot selection. Because there are multiple ways
	to partition a list, follow the algorithm on page 611.

	The values which are smaller than the pivot should be placed to the left of the pivot;
	the values which are larger than the pivot should be placed to the right of the pivot.

	@param left - left index for the subarray to partition.
	@param right - right index + 1 for the subarray to partition.
	@param pivotIndex - index of the pivot in the subarray.
	@return the pivot's ending index after the partition completes:
	Return -1 if	1) the array is empty,
	2) if any of the given indexes are out of bounds,
	3) if the left index is not less than the right index.
	*/
	virtual int partition(size_t left, size_t right, size_t pivotIndex)
	{
		if (left > right || right == left + 1 || right == left)
		{
			return -1;
		}

		else if (left >= currentSize || right > currentSize || left < 0 || right < 0)
		{
			return -1;
		}

		else if (pivotIndex < left || pivotIndex > right)
		{
			return -1;
		}

		size_t temp = arrayData[left];
		arrayData[left] = arrayData[pivotIndex];
		arrayData[pivotIndex] = temp;

		return pivotIndex;
	}

	/** @return: comma delimited string representation of the array. */
	virtual std::string toString() const
	{
		if (currentSize == 0)
		{
			return "Empty";
		}

		ostringstream quickSortOSS;

		for (size_t i = 0; i < currentSize; i++)
		{
			quickSortOSS << arrayData[i] << ", ";
		}

		return quickSortOSS.str();
	}

	friend std::ostream& operator<< (std::ostream& os, const QuickSort<T>& quickSort)
	{
		os << quickSort.toString();
		return os;
	};
};

#endif