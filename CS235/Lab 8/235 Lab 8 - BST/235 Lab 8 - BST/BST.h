#ifndef BST_H
#define BST_H
#include <iostream>
#include <string>
#include <sstream>
#include "BST_Interface.h"
using std::string;
using std::ostringstream;

/** A binary tree node with data, left and right child pointers */
template<typename T>
class BST : public BSTInterface<T>
{
private:
	struct Node
	{
		T data;
		Node* left;
		Node* right;
		Node(const T& inputData) : data(inputData), left(nullptr), right(nullptr) {};
	};

	Node* root;
	T predecessorValue;

public:
	BST()
	{
		root = nullptr;
	}

	~BST()
	{
		clearTree();
	}

	/** Return true if node added to BST, else false */
	virtual bool addNode(const T& data)
	{
		if (searchTree(root, data) == true)
		{
			return false;
		}

		if (root == nullptr)
		{
			root = new Node(data);
			return true;
		}

		return insertNode(root, data);
	}

	/** Return true if node removed from BST, else false */
	virtual bool removeNode(const T& data)
	{
		if (searchTree(root, data) == false)
		{
			return false;
		}

		return deleteNode(root, data);
	}

	/** Return true if BST cleared of all nodes, else false */
	virtual bool clearTree()
	{
		while (root != nullptr)
		{
			removeNode(root->data);
		}
		return true;
	}

	/** Return a level order traversal of a BST as a string */
	virtual string toString() const
	{
		ostringstream outSS;

		if (root == nullptr) outSS << " Empty";
		else
		{
			int level = -1;
			do
			{
				outSS << std::endl << "  " << ++level << ":";
			} while (outLevel(root, level, outSS));
		}
		return outSS.str();
	}

	/** Output nodes at a given level */
	bool outLevel(Node* root, int level, ostringstream& outSS) const
	{
		if (root == nullptr)
		{
			return false;
		}

		if (level == 0)
		{
			outSS << " " << root->data;
			if ((root->left != nullptr) || (root->right != nullptr)) return true;
			return false;
		}
		if ((level == 1) && !root->left && root->right) outSS << " _";
		bool left = outLevel(root->left, level - 1, outSS);
		bool right = outLevel(root->right, level - 1, outSS);
		if ((level == 1) && root->left && !root->right) outSS << " _";
		return left || right;
	}

	/** Searches the tree for a value */
	bool searchTree(Node*& currentNode, T value)
	{
		if (currentNode == nullptr)
		{
			return false;
		}

		else if (currentNode->data == value)
		{
			return true;
		}

		else if (value < currentNode->data)
		{
			return searchTree(currentNode->left, value);
		}

		else if (value > currentNode->data)
		{
			return searchTree(currentNode->right, value);
		}
	}

	/** Recursively finds the spot to add a node */
	bool insertNode(Node*& currentNode, T value)
	{
		if (value < currentNode->data)
		{
			if (currentNode->left == nullptr)
			{
				currentNode->left = new Node(value);
				return true;
			}

			return insertNode(currentNode->left, value);
		}

		else if (value > currentNode->data)
		{
			if (currentNode->right == nullptr)
			{
				currentNode->right = new Node(value);
				return true;
			}

			return insertNode(currentNode->right, value);
		}
	}

	bool deleteNode(Node*& currentNode, T value)
	{
		if (currentNode == nullptr)
		{
			return false;
		}

		if (value < currentNode->data)
		{
			return deleteNode(currentNode->left, value);
		}

		else if (value > currentNode->data)
		{
			return deleteNode(currentNode->right, value);
		}
		
		else
		{
			Node* temp = currentNode;

			if (currentNode->left == nullptr && currentNode->right == nullptr)
			{
				delete currentNode;
				currentNode = nullptr;
			}

			else if (currentNode->left == nullptr && currentNode->right != nullptr)
			{
				currentNode = currentNode->right;
				delete temp;
			}

			else if (currentNode->right == nullptr && currentNode->left != nullptr)
			{
				currentNode = currentNode->left;
				delete temp;
			}

			else
			{
				replaceParent(currentNode, currentNode->left);
			}
			return true;
		}
	}

	void replaceParent(Node*& parent, Node* child)
	{
		if (child->right != nullptr)
		{
			replaceParent(parent, child->right);
		}

		else
		{
			parent->data = child->data;
			deleteNode(parent->left, child->data);
		}
	}
};
#endif