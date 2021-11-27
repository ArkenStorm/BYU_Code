import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class ContactMain {
    public static void main(String[] args) {
        ContactManager manager = new ContactManager();
        manager.addContact(new Contact("Taylor", "Whitlock", "801-376-1947", "taylorcwhitlock@gmail.com"));
        manager.addContact(new Contact("Joe", "Shmoe", "224-2226", "joeshmoe@gmail.com"));
        manager.addContact(new Contact("Bob", "Burgundy", "555-555-5550", "bb@gmail.com"));
        manager.addContact(new Contact("Sav", "More", "1-800-SAV-MORE", "savmore@gmail.com"));

        ContactManagerAdapter adapter = new ContactManagerAdapter(manager);
        Writer writer = new PrintWriter(System.out);
        Table table = new Table(writer, adapter);

        try {
            table.display();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
