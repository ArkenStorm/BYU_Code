package spell;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {
	private Trie redwood = new Trie();

	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		Scanner scanner = new Scanner(new FileReader(dictionaryFileName));
		while (scanner.hasNext()) {
			redwood.add(scanner.next());
		}
	}

	@Override
	public String suggestSimilarWord(String inputWord) {
		inputWord = inputWord.toLowerCase();
		if (redwood.find(inputWord) != null) {
			return inputWord;
		}
		List<String> possibleChoices = new ArrayList<>();
		Map<String, Integer> validChoices = new HashMap<>();
		List<String> candidates = new ArrayList<>();
		candidates.add(inputWord);

		for (int editDistance = 1; editDistance <= 2; editDistance++) {
			for (String candidate : candidates) {
				for (int i = 0; i < candidate.length(); i++) {
					StringBuilder deleter = new StringBuilder(candidate);
					deleter.deleteCharAt(i);
					possibleChoices.add(deleter.toString());
				}

				for (int i = 0; i < candidate.length() - 1; i++) {
					StringBuilder transposer = new StringBuilder(candidate);
					char a = transposer.charAt(i);
					char b = transposer.charAt(i + 1);
					transposer.setCharAt(i, b);
					transposer.setCharAt(i + 1, a);
					possibleChoices.add(transposer.toString());
				}

				for (int i = 0; i < candidate.length(); i++) {
					StringBuilder replacer = new StringBuilder(candidate);
					for (char letter = 'a'; letter <= 'z'; letter++) {
						replacer.setCharAt(i, letter);
						possibleChoices.add(replacer.toString());
					}
				}

				for (int i = 0; i <= candidate.length(); i++) {
					for (char letter = 'a'; letter <= 'z'; letter++) {
						StringBuilder inserter = new StringBuilder(candidate);
						inserter.insert(i, letter);
						possibleChoices.add(inserter.toString());
					}
				}
			}

			for (String word : possibleChoices) {
				INode node = redwood.find(word);
				if (node != null) {
					validChoices.put(word, node.getValue());
				}
			}
			candidates.addAll(possibleChoices);
			possibleChoices.clear();

			if (!validChoices.isEmpty()) {
				int maxVal = 0;
				List<String> finalists = new ArrayList<>();
				for (Map.Entry<String, Integer> entry : validChoices.entrySet()) {
					if (entry.getValue() > maxVal) {
						maxVal = entry.getValue();
						finalists.clear();
					}
					if (entry.getValue() == maxVal) {
						finalists.add(entry.getKey());
					}
				}
				if (finalists.size() == 1) {
					return finalists.get(0);
				}
				finalists.sort(String::compareTo);
				return finalists.get(0);
			}
		}

		return null;
	}
}
