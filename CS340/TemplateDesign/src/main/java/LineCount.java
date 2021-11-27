import java.io.*;
import java.util.*;
import java.util.regex.*;

public class LineCount extends ParseTemplate {
	private int _totalLineCount;
	
	public LineCount(String dirName, String pattern, boolean recurse) {
		super(dirName, pattern, recurse);
		_totalLineCount = 0;
	}

	private void run() {
		parseDirectory(new File(this.dirName));
		printFinalResults();
	}

	@Override
	void printFinalResults() {
		System.out.println("TOTAL: " + _totalLineCount);
	}

	@Override
	void scanFile(File file, Scanner input, int currentCount) {
		while (input.hasNextLine()) {
			String line = input.nextLine();
			++_totalLineCount;
			++currentCount;
		}
	}

	@Override
	void printFileResults(int count, File file) {
		System.out.println(count + "  " + file);
	}
	
	public static void main(String[] args) {
		String directory = "";
		String pattern = "";
		boolean recurse = false;
		
		if (args.length == 2) {
			recurse = false;
			directory = args[0];
			pattern = args[1];
		}
		else if (args.length == 3 && args[0].equals("-r")) {
			recurse = true;
			directory = args[1];
			pattern = args[2];
		}
		else {
			usage();
			return;
		}
		
		LineCount lineCounter = new LineCount(directory, pattern, recurse);
		lineCounter.run();
	}
	
	private static void usage() {
		System.out.println("USAGE: java LineCount {-r} <dir> <file-pattern>");
	}

}
