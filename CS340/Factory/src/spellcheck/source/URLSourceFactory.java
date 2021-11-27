package spellcheck.source;

public class URLSourceFactory implements SourceFactory {
    @Override
    public DocumentSource buildSource() {
        return new URLFetcher();
    }
}
