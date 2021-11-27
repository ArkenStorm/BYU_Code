
package spellcheck;

import spellcheck.dictionary.iDictionary;
import spellcheck.extractor.Extractor;
import spellcheck.source.DocumentSource;

import java.io.IOException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


public class SpellingChecker {
    private final DocumentSource source;
    private final Extractor extractor;
    private final iDictionary dictionary;

    public SpellingChecker(DocumentSource source, Extractor extractor, iDictionary dictionary) {
        this.source = source;
        this.extractor = extractor;
        this.dictionary = dictionary;
    }

    public SortedMap<String, Integer> check(String doc) throws IOException {

		// download the document content
		String content = source.getContent(doc);

		// extract words from the content
		//
		List<String> words = extractor.extract(content);

		// find spelling mistakes
		//
		SortedMap<String, Integer> mistakes = new TreeMap<>();

        for (String word : words) {
            if (!dictionary.isValidWord(word)) {
                if (mistakes.containsKey(word)) {
                    int oldCount = mistakes.get(word);
                    mistakes.put(word, oldCount + 1);
                } else {
                    mistakes.put(word, 1);
                }
            }
        }

		return mistakes;
	}
}

