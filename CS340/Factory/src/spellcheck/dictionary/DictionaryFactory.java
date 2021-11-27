package spellcheck.dictionary;

import java.io.IOException;

public interface DictionaryFactory {
    iDictionary buildDictionary(String fileLocation) throws IOException;
}
