package hangman;

import com.sun.source.tree.Tree;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
	public EvilHangmanGame() {
		this.wordList = new TreeSet<>();
		this.usedLetters = new TreeSet<>();
		this.wordPartitions = new TreeMap<>();
	}

	private Set<String> wordList;
	private SortedSet<Character> usedLetters;
	private Map<String, Set<String>> wordPartitions;
	private int wordLength;
	private String currentWord;

	@Override
	public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
		wordList.clear();
		this.wordLength = wordLength;
		currentWord = "-".repeat(wordLength);
		if (dictionary.length() == 0) {
			throw new EmptyDictionaryException();
		}
		else {
			Scanner scanner = new Scanner(new FileReader(dictionary));
			while (scanner.hasNext()) {
				String word = scanner.next();
				if (word.length() == wordLength) {
					wordList.add(word.toLowerCase());
				}
			}
			if (wordList.isEmpty()) {
				throw new EmptyDictionaryException();
			}
		}
	}

	@Override
	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
		guess = Character.toLowerCase(guess);
		if (getGuessedLetters().contains(guess)) {
			throw new GuessAlreadyMadeException();
		}
		usedLetters.add(guess);
		for (String word : wordList) {
			StringBuilder builder = new StringBuilder();
			for (char letter : word.toCharArray()) {
				builder.append(letter == guess ? letter : '-');
			}
			if (wordPartitions.containsKey(builder.toString())) {
				wordPartitions.get(builder.toString()).add(word);
			}
			else {
				Set<String> newSet = new TreeSet<>();
				newSet.add(word);
				wordPartitions.put(builder.toString(), newSet);
			}
		}

		int maxVal = 0;
		Map<String, Set<String>> largestPartitions = new TreeMap<>();
		for (Map.Entry<String, Set<String>> partition : wordPartitions.entrySet()) {
			if (partition.getValue().size() > maxVal) {
				largestPartitions.clear();
				maxVal = partition.getValue().size();
				largestPartitions.put(partition.getKey(), partition.getValue());
			}
			else if (partition.getValue().size() == maxVal) {
				largestPartitions.put(partition.getKey(), partition.getValue());
			}
		}
		if (largestPartitions.size() > 1) {
			int fewest = wordLength;
			Map<String, Set<String>> fewestLetters = new TreeMap<>();
			for (Map.Entry<String, Set<String>> partition : largestPartitions.entrySet()) {
				int occurrences = 0;
				String key = partition.getKey();
				for (int i = 0; i < key.length(); i++) {
					if (key.charAt(i) == guess) {
						occurrences += 1;
					}
				}
				if (occurrences < fewest) {
					fewest = occurrences;
					fewestLetters.clear();
					fewestLetters.put(key, partition.getValue());
				}
				else if (occurrences == fewest) {
					fewestLetters.put(key, partition.getValue());
				}
			}
			if (fewestLetters.size() > 1) {
				Map<String, Set<String>> rightmost = new TreeMap<>();
				int startingPostition = wordLength - 1;
				do {
					int rightmostIndex = 0;
					for (Map.Entry<String, Set<String>> partition : fewestLetters.entrySet()) {
						String key = partition.getKey();
						for (int i = startingPostition; i >= 0; i--) {
							if (key.charAt(i) == guess) {
								if (i > rightmostIndex) {
									rightmostIndex = i;
									rightmost.clear();
									rightmost.put(key, partition.getValue());
								} else if (i == rightmostIndex) {
									rightmost.put(key, partition.getValue());
								}
							}
						}
					}
					startingPostition = rightmostIndex - 1;
				} while (rightmost.size() > 1);
				wordList = rightmost.values().iterator().next();
				updateCurrentWord(rightmost.keySet().iterator().next());
			}
			else {
				wordList = fewestLetters.values().iterator().next();
				updateCurrentWord(fewestLetters.keySet().iterator().next());
			}
		}
		else {
			wordList = largestPartitions.values().iterator().next();
			updateCurrentWord(largestPartitions.keySet().iterator().next());
		}
		wordPartitions.clear();
		return wordList;
	}

	@Override
	public SortedSet<Character> getGuessedLetters() {
		return usedLetters;
	}

	public String getCurrentWord() {
		return currentWord;
	}

	public void setCurrentWord(String word) {
		currentWord = word;
	}

	private void updateCurrentWord(String newKey) {
		StringBuilder newWord = new StringBuilder(currentWord);
		StringBuilder replacer = new StringBuilder(newKey);
		for (int i = 0; i < replacer.length(); i++) {
			if (replacer.charAt(i) != '-') {
				newWord.setCharAt(i, replacer.charAt(i));
			}
		}
		currentWord = newWord.toString();
	}
}
