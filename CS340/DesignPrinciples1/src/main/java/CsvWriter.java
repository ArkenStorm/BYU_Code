/*1. What design principles does this program violate?
        Single Responsibility Principle
  2. Refactor the program to improve its design.

        CsvWriter.java

 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {
    private BufferedWriter writer;

    public CsvWriter() {
    }

    public void write(String[][] lines, String filename) throws IOException {
        writer = new BufferedWriter(new FileWriter(filename));
        for (int i = 0; i < lines.length; i++) {
            writeLine(lines[i]);
        }
        writer.close();
    }

    private void writeLine(String[] fields) throws IOException {
        if (fields.length == 0)
            writer.write("\n");
        else {

            writeField(fields[0]);

            for (int i = 1; i < fields.length; i++) {
                writer.write(",");
                writeField(fields[i]);
            }
            writer.write("\n");
        }
    }

    private void writeField(String field) throws IOException {
        if (field.indexOf(',') != -1 || field.indexOf('\"') != -1)
            writeQuoted(field);
        else
            writer.write(field);
    }

    private void writeQuoted(String field) throws IOException {
        writer.write('\"');
        for (int i = 0; i < field.length(); i++) {
            char c = field.charAt(i);
            if (c == '\"')
                System.out.print("\"\"");
            else
                System.out.print(c);
        }
        writer.write('\"');
    }
}
