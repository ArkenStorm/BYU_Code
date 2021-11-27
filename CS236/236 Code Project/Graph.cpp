#include <iostream>
#include "Graph.h"

using namespace std;

Graph::Graph(vector<Rule>& ruleList) {
	for (size_t i = 0; i < ruleList.size(); ++i) {
		Node ruleNode;
		allNodes.push_back(ruleNode);
	}
	postOrderedNodes.resize(allNodes.size());
	currPONumber = 0;
}

vector<int> Graph::DFSForest() {
	for (Node& node : allNodes) {
		node.ClearFlag();
	}
	size_t i = 0;
	for (Node& node : allNodes) {
		if (!node.GetFlag()) {
			DFS(node, i);
		}
		++i;
	}
	return postOrderedNodes;
}

void Graph::DFS(Node& currentNode, int currentNodeIndex) {
	currentNode.SetFlag();
	for (int adjacentNodeIndex : currentNode.GetAdjacentNodes()) {
		if (!(allNodes.at(adjacentNodeIndex).GetFlag())) {
			DFS(allNodes.at(adjacentNodeIndex), adjacentNodeIndex);
		}
	}
	postOrderedNodes.at(currentNodeIndex) = currPONumber++;
}

void Graph::DFSFindSCC(int nodeIndex, vector<set<int>>& SCC) {
	SCC.back().insert(nodeIndex);
	Node& currentNode = allNodes.at(nodeIndex);
	currentNode.SetFlag();
	for (int adjacentNodeIndex : currentNode.GetAdjacentNodes()) {
		if (!(allNodes.at(adjacentNodeIndex).GetFlag())) {
			DFSFindSCC(adjacentNodeIndex, SCC);
		}
	}
}

void Graph::DFSForestWithSCC(const vector<int>& postOrderedNodes, vector<set<int>>& SCC) {
	const size_t numNodes = postOrderedNodes.size();
	vector<int> evaluationOrder(numNodes);
	for (size_t i = 0; i < numNodes; ++i) {
		evaluationOrder.at(numNodes - (postOrderedNodes.at(i) + 1)) = i;
	}
	for (size_t i = 0; i < evaluationOrder.size(); ++i) {
		if (!(allNodes.at(evaluationOrder.at(i)).GetFlag())) {
			SCC.push_back(set<int>());
			DFSFindSCC(evaluationOrder.at(i), SCC);
		}
	}
}

void Graph::CreateAdjacencies(vector<Rule>& ruleList) {
	for (size_t i = 0; i < allNodes.size(); ++i) {
		for (Predicate& bodyPredicate : ruleList.at(i).GetAllPredicates()) {
			for (size_t j = 0; j < ruleList.size(); ++j) {
				if (bodyPredicate.GetID() == ruleList.at(j).GetHeadPredicate().GetID()) {
					allNodes.at(i).AddAdjacentNode(j);
				}
			}
		}
	}
}

void Graph::CreateReverseAdjacencies(const Graph& originalGraph) {
	for (size_t i = 0; i < originalGraph.allNodes.size(); ++i) {
		for (int nodeIndex : originalGraph.allNodes.at(i).GetAdjacentNodes()) {
			this->allNodes.at(nodeIndex).AddAdjacentNode(i);
		}
	}
}

void Graph::ToString() {
	cout << "Dependency Graph" << endl;
	for (size_t i = 0; i < allNodes.size(); ++i) {
		cout << "R" << i << ":";
		string formatter = "R";
		for (int ruleNumber : allNodes.at(i).GetAdjacentNodes()) {
			cout << formatter << ruleNumber;
			formatter = ",R";
		}
		cout << endl;
	}
	cout << endl;
}
