package spell;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class OldTrie implements ITrie {
	private Node root = new Node();
	private int wordCount = 0;
	private int nodeCount = 1;
	private Set<String> dictionary = new TreeSet<>();

	@Override
	public void add(String word) {
		dictionary.add(word);
		Node currNode = root;
		for (char c : word.toCharArray()) {
			currNode = currNode.birthChild(c);
		}
		currNode.incrementFrequency();
		if (currNode.getValue() == 1) {
			wordCount++;
		}
	}

	@Override
	public INode find(String word) {
		Node currNode = root;
		for (char c : word.toCharArray()) {
			currNode = currNode.getChild(c);
			if (currNode == null) {
				return null;
			}
		}
		if (currNode.getValue() < 1) {
			return null;
		}
		return currNode;
	}

	@Override
	public int getWordCount() {
		return wordCount;
	}

	@Override
	public int getNodeCount() {
		return nodeCount;
	}

	@Override
	public String toString() {
		return String.join("\n", dictionary);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Trie trie = (Trie) o;
		return wordCount == trie.wordCount &&
				nodeCount == trie.nodeCount &&
				Objects.equals(root, trie.root);
	}

	@Override
	public int hashCode() {
		return Objects.hash(root, wordCount, nodeCount);
	}

	public class Node implements INode {
		private int frequency = 0;
		private Node[] children = new Node[26];

		@Override
		public int getValue() {
			return frequency;
		}

		public void incrementFrequency() {
			frequency++;
		}

		public Node birthChild(char key) {
			if (children[key - 'a'] == null) {
				children[key - 'a'] = new Node();
				nodeCount++;
			}
			return children[key - 'a'];
		}

		public Node getChild(char key) {
			return children[key - 'a'];
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Node node = (Node) o;
			return frequency == node.frequency &&
					Arrays.equals(children, node.children);
		}

		@Override
		public int hashCode() {
			int result = Objects.hash(frequency);
			result = 31 * result + Arrays.hashCode(children);
			return result;
		}
	}
}
