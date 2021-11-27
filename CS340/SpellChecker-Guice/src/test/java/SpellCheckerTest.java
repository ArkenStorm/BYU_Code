import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.SortedMap;

public class SpellCheckerTest {
    @Test
    public void testInjection() {
        try {
            String doc = "https://pastebin.com/raw/t6AZ5kx3";
            Injector injector = Guice.createInjector(new TestGuicer());
            SpellingChecker checker = injector.getInstance(SpellingChecker.class);
            SortedMap<String, Integer> mistakes = checker.check(doc);
            assert mistakes.size() == 0;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
