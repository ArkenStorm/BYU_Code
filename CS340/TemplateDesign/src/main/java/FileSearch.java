import java.io.*;
import java.util.*;
import java.util.regex.*;

public class FileSearch extends ParseTemplate {
	private Matcher _searchMatcher;
	private int _totalMatches;
	
	public FileSearch(String dirName, String filePattern, String searchPattern, boolean recurse) {
		super(dirName, filePattern, recurse);
		_searchMatcher = Pattern.compile(searchPattern).matcher("");
		_totalMatches = 0;
		
		parseDirectory(new File(this.dirName));
		
		printFinalResults();
	}

	@Override
	void printFileResults(int matches, File file) {
		if (matches > 0) {
			System.out.println("MATCHES: " + matches);
		}
	}

	@Override
	void scanFile(File file, Scanner input, int currentCount) {
		while (input.hasNextLine()) {
			String line = input.nextLine();

			_searchMatcher.reset(line);
			if (_searchMatcher.find()) {
				if (++currentCount == 1) {
					System.out.println("");
					System.out.println("FILE: " + file);
				}

				System.out.println(line);
				++_totalMatches;
			}
		}
	}

	@Override
	void printFinalResults() {
		System.out.println("");
		System.out.println("TOTAL MATCHES: " + _totalMatches);
	}

	public static void main(String[] args) {
		
		String dirName = "";
		String filePattern = "";
		String searchPattern = "";
		boolean recurse = false;
		
		if (args.length == 3) {
			recurse = false;
			dirName = args[0];
			filePattern = args[1];
			searchPattern = args[2];
		}
		else if (args.length == 4 && args[0].equals("-r")) {
			recurse = true;
			dirName = args[1];
			filePattern = args[2];
			searchPattern = args[3];
		}
		else {
			usage();
			return;
		}
		
		new FileSearch(dirName, filePattern, searchPattern, recurse);
	}
	
	private static void usage() {
		System.out.println("USAGE: java FileSearch {-r} <dir> <file-pattern> <search-pattern>");
	}
}
