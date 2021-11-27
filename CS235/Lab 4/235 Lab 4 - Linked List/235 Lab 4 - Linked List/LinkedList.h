#ifndef LINKED_LIST_H
#define LINKED_LIST_H

#include <iostream>
#include <string>
#include <sstream>
#include "LinkedListInterface.h"

template <typename T>
class LinkedList : public LinkedListInterface<T>
{
public:
	LinkedList()
	{
		this->head = nullptr;
	}

	~LinkedList()
	{
		clear();
	}

	/** Insert Node at beginning of linked list (no duplicates) */
	virtual bool insertHead(T value)
	{
		if (IsValueInList(value) == false)
		{
			Node* tempPtr = head;
			head = new Node(value, tempPtr);
			return true;
		}

		else { return false; }
	}

	/** Insert Node at end of linked list (no duplicates) */
	virtual bool insertTail(T value)
	{
		if (IsValueInList(value) == false)
		{
			Node* tempPtr = head;
			if (tempPtr == nullptr) { insertHead(value); }

			else
			{
				while (tempPtr->next != nullptr)		// goes through the list to find the end and puts a new node there
				{
					tempPtr = tempPtr->next;
				}

				tempPtr->next = new Node(value);
			}
		}

		else { return false; }
	}

	/** Insert node after matchNode (no duplicates) */
	virtual bool insertAfter(T matchNode, T node)
	{
		if (IsValueInList(node) == false)
		{
			if (IsValueInList(matchNode) == true)
			{
				Node* tempPtr = head;
				while (tempPtr->data != matchNode)
				{
					tempPtr = tempPtr->next;
				}

				Node* otherTempPtr = tempPtr->next;
				tempPtr->next = new Node(node, otherTempPtr);
				return true;
			}

			else { return false; }
		}
		else { return false; }
	}

	/** Remove Node from linked list */
	virtual bool remove(T value)
	{
		if (IsValueInList(value) == true)
		{
			Node* tempPtr = head;
			if (head->data == value)
			{
				head = head->next;
				delete tempPtr;
				return true;
			}

			else
			{
				while (tempPtr->next->data != value)
				{
					tempPtr = tempPtr->next;
				}

				Node* otherTempPtr = tempPtr->next;
				tempPtr = otherTempPtr->next;
				delete otherTempPtr;
				return true;
			}
		}

		else { return false; }
	}

	/** Remove all Nodes from linked list */
	virtual bool clear()
	{
		while (head != nullptr)
		{
			remove(head->data);
		}
		return true;
	}

	/** Return Node at index (0 based) */
	virtual T at(int index)
	{
		Node* tempPtr = head;
		if (index >= 0 && index < size())
		{
			for (int i = 0; i < index; ++i)
			{
				tempPtr = tempPtr->next;
			}
			return tempPtr->data;
		}
		
		else { return T(); }
	}

	/** Returns the number of nodes in the linked list */
	virtual int size() const
	{
		int count = 0;
		Node* tempPtr = head;
		while (tempPtr != nullptr)
		{
			++count;
			tempPtr = tempPtr->next;
		}

		return count;
	}

	bool IsValueInList(T checkData)
	{
		Node* nodePtr = head;

		while (nodePtr != nullptr)
		{
			if (nodePtr->data == checkData) { return true; }
			else { nodePtr = nodePtr->next; }
		}

		return false;
	}

	std::string PrintList()
	{
		std::ostringstream outSS;
		Node* tempPtr = head;

		if (tempPtr == nullptr) { return ""; }

		while (tempPtr->next != nullptr)			// puts a space after every node data until the last one
		{
			outSS << tempPtr->data << " ";
			tempPtr = tempPtr->next;
		}

		outSS << tempPtr->data;
		return outSS.str();
	}

private:
	struct Node
	{
		T data;
		Node* next;
		Node(const T& inputData) : data(inputData), next(nullptr) {};
		Node(const T& inputData, Node* nodePtr) : data(inputData), next(nodePtr) {};
	};

	Node* head;
};

#endif