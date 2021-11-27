package spellcheck;

import java.io.IOException;

public interface DocumentSource {
    String getContent(String document) throws IOException;
}
