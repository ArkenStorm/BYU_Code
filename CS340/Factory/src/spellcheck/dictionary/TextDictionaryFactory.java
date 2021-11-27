package spellcheck.dictionary;

import java.io.IOException;

public class TextDictionaryFactory implements DictionaryFactory {
    public iDictionary buildDictionary(String fileLocation) throws IOException {
        return new Dictionary(fileLocation);
    }
}
