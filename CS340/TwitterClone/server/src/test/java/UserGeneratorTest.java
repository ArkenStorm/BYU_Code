import dao.BigScaryDataGenerator;
import org.junit.jupiter.api.Test;

class UserGeneratorTest {

    @Test
    void run() {
        BigScaryDataGenerator generator = new BigScaryDataGenerator();
        generator.GenerateUsersAndFollows();
    }
}
