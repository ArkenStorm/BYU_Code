#ifndef GRAPH_H
#define GRAPH_H
#include <vector>
#include <set>
#include <map>
#include "Rule.h"
using namespace std;
class Graph {
public:
	class Node {
	public:
		void SetFlag() { visitFlag = true; }
		void ClearFlag() { visitFlag = false; }
		void AddAdjacentNode(int nodeIndex) { adjacentNodeIndices.insert(nodeIndex); }
		bool GetFlag() const { return visitFlag; }
		const set<int>& GetAdjacentNodes() const { return adjacentNodeIndices; }

	private:
		bool visitFlag;
		set<int> adjacentNodeIndices;
	};

	Graph(vector<Rule>& ruleList);
	vector<int> DFSForest();
	void DFS(Node& currentNode, int currentNodeIndex);
	void DFSFindSCC(int nodeIndex, vector<set<int>>& SCC);
	void DFSForestWithSCC(const vector<int>& postOrderedNodes, vector<set<int>>& SCC);
	void CreateAdjacencies(vector<Rule>& ruleList);
	void CreateReverseAdjacencies(const Graph& originalGraph);
	Node& GetNode(int index) { return allNodes.at(index); }
	void ToString();

private:
	int currPONumber;
	vector<Node> allNodes;
	vector<int> postOrderedNodes;
};
#endif
