
package spellcheck;

import java.io.IOException;
import java.net.URL;
import java.util.SortedMap;


public class Main {

	public static void main(String[] args) {
	
		try {
			String doc = args[0];
			SpellingChecker checker = new SpellingChecker(new URLFetcher(), new WordExtractor(), new Dictionary("dict.txt"));
			SortedMap<String, Integer> mistakes = checker.check(doc);
			System.out.println(mistakes);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

