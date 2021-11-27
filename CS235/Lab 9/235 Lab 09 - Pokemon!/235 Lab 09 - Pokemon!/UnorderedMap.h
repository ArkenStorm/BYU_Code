#ifndef UNORDERED_MAP_H
#define UNORDERED_MAP_H
#include <string>
#include <iostream>
#include <utility>
#include "MapInterface.h"
using namespace std;

template <typename K, typename V>
class UnorderedMap : public MapInterface<K,V>
{
public:
	static const int HashTableSize = 31;
	static const int BonusHashTableSize = 7;

	UnorderedMap() {}
	~UnorderedMap() {}

	/** Read/write index access operator.
	If the key is not found, an entry is made for it.
	@return: Read and write access to the value mapped to the provided key. */
	virtual V& operator[](const K& key)
	{
		for (size_t i = 0; i < HashTableSize; i++)
		{
			if (hashArray[i].first == key)
			{
				return hashArray[i].second;
			}
		}


	}

	/** @return: the number of elements removed from the Map. */
	virtual size_t erase(const K& key)
	{
		return 0;
	}

	/** Removes all items from the Map. */
	virtual void clear()
	{

	}

	/** @return: number of Key-Value pairs stored in the Map. */
	virtual size_t size() const
	{
		return 0;
	}

	/** @return: maximum number of Key-Value pairs that the Map can hold. */
	virtual size_t max_size() const
	{
		return 0;
	}

	/** @return: string representation of Key-Value pairs in Map. */
	virtual std::string toString() const
	{
		return "reer";
	}

	friend std::ostream& operator<< (std::ostream& os, const UnorderedMap<K, V> pokemonMap)
	{
		os << pokemonMap.toString();
		return os;
	};

private:
	pair<K, V> hashArray[HashTableSize];
};
#endif