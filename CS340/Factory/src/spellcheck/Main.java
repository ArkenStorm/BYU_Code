
package spellcheck;

import spellcheck.dictionary.Dictionary;
import spellcheck.dictionary.DictionaryFactory;
import spellcheck.dictionary.TextDictionaryFactory;
import spellcheck.dictionary.iDictionary;
import spellcheck.extractor.Extractor;
import spellcheck.extractor.WordExtractorFactory;
import spellcheck.source.DocumentSource;
import spellcheck.source.URLSourceFactory;

import java.io.IOException;
import java.util.SortedMap;


public class Main {

	public static void main(String[] args) {
	
		try {
			String doc = args[0];
			DocumentSource source = new URLSourceFactory().buildSource();
			Extractor extractor = new WordExtractorFactory().buildExtractor();
			iDictionary dict = new TextDictionaryFactory().buildDictionary("dict.txt");
			SpellingChecker checker = new SpellingChecker(source, extractor, dict);
			SortedMap<String, Integer> mistakes = checker.check(doc);
			System.out.println(mistakes);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

