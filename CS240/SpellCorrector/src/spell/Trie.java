package spell;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Trie implements ITrie {
	private Node root = new Node();

	@Override
	public void add(String word) {
		Node currNode = root;
		for (char c : word.toCharArray()) {
			currNode = currNode.birthChild(c);
		}
		currNode.incrementFrequency();
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
		return getWords(root);
	}

	private int getWords(Node node) {
		int wordCount = 0;
		for (Node child : node.children) {
			if (child != null) {
				wordCount += getWords(child);
			}
		}
		return wordCount + (node.getValue() >= 1 ? 1 : 0);
	}

	@Override
	public int getNodeCount() {
		return getNodes(root);
	}

	private int getNodes(Node node) {
		int nodeCount = 1;
		for (Node child : node.children) {
			if (child != null) {
				nodeCount += getNodes(child);
			}
		}
		return nodeCount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String prefix = "";
		String dictionary = toString(root, prefix, builder);
		return dictionary.substring(0, dictionary.length() - 1);
	}

	private String toString(Node node, String prefix, StringBuilder builder) {
		if (node.getValue() >= 1) {
			builder.append(prefix).append('\n');
		}
		for (int i = 0; i < 26; i++) {
			if (node.children[i] != null) {
				toString(node.children[i], prefix + (char) ('a' + i), builder);
			}
		}
		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Trie trie = (Trie) o;
		return root.equals(trie.root);
	}

	@Override
	public int hashCode() {
		return Objects.hash(root);
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
