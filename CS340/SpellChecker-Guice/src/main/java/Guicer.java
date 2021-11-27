import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.io.IOException;

public class Guicer extends AbstractModule {
    @Override
    protected void configure() {
        bind(Extractor.class).to(WordExtractor.class);
        bind(DocumentSource.class).to(URLFetcher.class);
    }

    @Provides
    iDictionary provideDictionary() throws IOException {
        return new Dictionary("dict.txt");
    }
}
