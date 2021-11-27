package spellcheck.extractor;

public class WordExtractorFactory implements ExtractorFactory {
    @Override
    public Extractor buildExtractor() {
        return new WordExtractor();
    }
}
