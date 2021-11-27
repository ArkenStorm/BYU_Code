/*1. What design principles does this code violate?
    There is code duplication between client2 and client4, the naming is terrible and non-descriptive.
    All the person variables should be private and have getters.   
 

2. Refactor the code to improve its design.


        Person.java
*/

public class Person {
    private String last;
    private String first;
    private String middle;

    public Person(String last, String first, String middle) {
        this.last = last;
        this.first = first;
        this.middle = middle;
    }

    public String getLast() {
        return last;
    }

    public String getFirst() {
        return first;
    }

    public String getMiddle() {
        return middle;
    }
}

//PersonClient.java

// The clients are in one file for convenience;
// imagine them as non-test methods in separate client classes.

        import junit.framework.TestCase;

        import java.io.*

public class PersonClient extends TestCase {
    public PersonClient(String name) {super(name);}

    public void client1(Writer out, Person person) throws IOException {
        out.write(person.getFirst());
        out.write(" ");
        if (person.getMiddle() != null) {
            out.write(person.getMiddle());
            out.write(" ");
        }
        out.write(person.getLast));
    }

    public String client2(Person person) {
        String result = person.getLast) + ", " + person.getFirst();
        if (person.getMiddle() != null)
            result += " " + person.getMiddle();
        return result;
    }

    public void client3(Writer out, Person person) throws IOException {
        out.write(person.getLast));
        out.write(", ");
        out.write(person.getFirst());
        if (person.getMiddle() != null) {
            out.write(" ");
            out.write(person.getMiddle());
        }
    }

    public void testClients() throws IOException {
        Person bobSmith = new Person("Smith", "Bob", null);
        Person jennyJJones = new Person("Jones", "Jenny", "J");
        StringWriter out = new StringWriter();
        client1(out, bobSmith);
        assertEquals("Bob Smith", out.toString());

        out = new StringWriter();
        client1(out, jennyJJones);
        assertEquals("Jenny J Jones", out.toString());

        assertEquals("Smith, Bob", client2(bobSmith));
        assertEquals("Jones, Jenny J", client2(jennyJJones));

        out = new StringWriter();
        client3(out, bobSmith);
        assertEquals("Smith, Bob", out.toString());

        out = new StringWriter();
        client3(out, jennyJJones);
        assertEquals("Jones, Jenny J", out.toString());
    }
}