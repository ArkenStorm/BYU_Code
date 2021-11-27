import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class ParseTemplate {
    protected String dirName;
    protected String pattern;
    protected boolean recurse;
    protected Matcher fileMatcher;

    public ParseTemplate(String dirName, String pattern, boolean recurse) {
        this.dirName = dirName;
        this.pattern = pattern;
        this.recurse = recurse;
        this.fileMatcher = Pattern.compile(this.pattern).matcher("");
    }

    abstract void printFileResults(int count, File file);

    final protected void parseDirectory(File dir) {
        if (dir.isDirectory()) {
            if (dir.canRead()) {
                for (File file : dir.listFiles()) {
                    if (file.isFile() && file.canRead()) {
                        parseFile(file);
                    }
                }
                if (recurse) {
                    for (File file : dir.listFiles()) {
                        parseDirectory(file);
                    }
                }
            }
            else {
                System.out.println("Directory " + dir + " is unreadable");
            }
        }
        else {
            System.out.println(dir + " is not a directory");
        }
    }

    protected void parseFile(File file) {
        String fileName = "";
        try {
            fileName = file.getCanonicalPath();
        }
        catch (IOException e) {
            System.out.println("Cannot get canonical file path.");
        }
        fileMatcher.reset(fileName);
        if (fileMatcher.find()) {
            int currentCount = 0;
            try (Reader reader = new BufferedReader(new FileReader(file))) {
                scanFile(file, new Scanner(reader), currentCount);
            } catch (IOException e) {
                System.out.println("File " + file + " is unreadable");
            }
            finally {
                printFileResults(currentCount, file);
            }
        }
    }

    abstract void scanFile(File file, Scanner input, int currentCount);
    abstract void printFinalResults();
}
