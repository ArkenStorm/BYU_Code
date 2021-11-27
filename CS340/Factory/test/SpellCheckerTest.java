import org.junit.Test;
import spellcheck.SpellingChecker;
import spellcheck.dictionary.TextDictionaryFactory;
import spellcheck.dictionary.iDictionary;
import spellcheck.extractor.Extractor;
import spellcheck.extractor.WordExtractorFactory;
import spellcheck.source.DocumentSource;
import spellcheck.source.URLSourceFactory;

import java.util.SortedMap;

public class SpellCheckerTest {
    @Test
    public void testMistakes() {
        try {
            DocumentSource source = new URLSourceFactory().buildSource();
            Extractor extractor = new WordExtractorFactory().buildExtractor();
            iDictionary dict = new TextDictionaryFactory().buildDictionary("dict.txt");
            SpellingChecker checker = new SpellingChecker(source, extractor, dict);
            SortedMap<String, Integer> mistakes = checker.check("https://pastebin.com/raw/t6AZ5kx3");
            assert mistakes.size() == 4;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}