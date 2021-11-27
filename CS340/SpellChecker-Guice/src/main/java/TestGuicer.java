import com.google.inject.AbstractModule;
import com.google.inject.Inject;

public class TestGuicer extends AbstractModule {
    @Override
    protected void configure() {
        bind(Extractor.class).to(WordExtractor.class);
        bind(DocumentSource.class).to(URLFetcher.class);
        bind(iDictionary.class).to(MockDictionary.class);
    }
}
