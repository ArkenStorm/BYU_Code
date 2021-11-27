#ifndef SET_H
#define SET_H
#include <string>
#include <iostream>
#include <sstream>
#include <utility>
#include "SetInterface.h"
#include "BST.h"
using namespace std;

template<typename T>
class Set: public SetInterface<T>
{
private:
	BST<T> PokemonTree;
	size_t setSize;

public:
	Set() {}
	~Set() {}

	/** Inserts item into the set, if the container doesn't
	already contain an element with an equivalent value.
	@return: pair.first = pointer to item
	pair.second = true if successfully inserted, else false. */
	//virtual Pair<T*, bool> insert(const T& item) = 0;
	virtual bool insert(const T& item)
	{
		setSize += 1;
		return PokemonTree.addNode(item);
	}


	/** @return: the number of elements removed from the Set. */
	virtual size_t erase(const T& item)
	{
		bool deleteBool = PokemonTree.deleteNode(item);
		if (deleteBool == false)
		{
			return 0;
		}

		setSize -= 1;
		return 1;
	}

	/** Removes all items from the set. */
	virtual void clear()
	{
		setSize = 0;
		return PokemonTree.clearTree();
	}

	/** @return: the number of elements contained by the Set. */
	virtual size_t size() const
	{
		return setSize;
	}

	/** @return: return 1 if contains element equivalent to item, else 0. */
	virtual size_t count(const T& item) const
	{
		bool searchBool = PokemonTree.searchTree(PokemonTree.root, item);

		if (searchBool == true)
		{
			return 1;
		}

		return 0;
	}

	/** @return: string representation of items in Set. */
	virtual std::string toString() const
	{
		ostringstream outSS;

		if (PokemonTree.getRoot() == nullptr)
		{
			outSS << "Empty" << endl;
		}

		else
		{
			
		}

		return outSS.str();
	}

	friend std::ostream& operator<< (std::ostream& os, const Set<T> pokemonSet)
	{
		os << pokemonSet.toString();
		return os;
	};
};
#endif