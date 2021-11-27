package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangman {

    public static void main(String[] args) throws IOException, EmptyDictionaryException {
        try {
            File dictionary = new File(args[0]);
            EvilHangmanGame game = new EvilHangmanGame();
            int wordLength = Integer.parseInt(args[1]);
            int guesses = Integer.parseInt(args[2]);
			Set<String> possibleWords = new TreeSet<>();
            game.startGame(dictionary, wordLength);

            String currentWord = "-".repeat(wordLength);
            game.setCurrentWord(currentWord);
            Scanner scanner = new Scanner(System.in);
            for (int i = guesses; i > 0; i--) {
            	if (i > 1) {
					System.out.printf("You have %d guesses left\n", i);
				}
            	else {
					System.out.println("You have 1 guess left");
				}
                System.out.printf("Used letters: %s\n", usedLettersToString(game.getGuessedLetters()));
                System.out.printf("Word: %s\n", currentWord);
				System.out.println("Enter guess: ");
                String guess = scanner.nextLine();
				while (guess.length() != 1 || !Character.isLetter(guess.charAt(0))) {
					System.out.println("Invalid Input.\nEnter guess: ");
					guess = scanner.nextLine();
				}
                try {
					possibleWords = game.makeGuess(guess.charAt(0));
					currentWord = game.getCurrentWord();
					if (currentWord.indexOf(guess.charAt(0)) != -1) {
						i++;
						int occurrences = 0;
						for (char letter : currentWord.toCharArray()) {
							if (letter == guess.charAt(0)) {
								occurrences++;
							}
						}
						if (occurrences == 1) {
							System.out.printf("Yes, there is 1 %s\n\n", guess.charAt(0));
						}
						else {
							System.out.printf("Yes, there are %d %s's\n\n", occurrences, guess.charAt(0));
						}
					}
					else {
						System.out.printf("Sorry, there are no %s's\n\n", guess.charAt(0));
					}
				}
                catch (GuessAlreadyMadeException e) {
                	System.out.println("You already used that letter");
                	i++;
				}
                if (currentWord.indexOf('-') == -1) {
                	System.out.println("You win! ...Somehow...");
					System.out.printf("The word was: %s\n", currentWord);
                	break;
				}
            }
			if (currentWord.indexOf('-') != -1) {
				System.out.println("You lose!");
				ArrayList<String> randomizer = new ArrayList<>(possibleWords);
				System.out.printf("The word was: %s\n", randomizer.get(new Random().nextInt(randomizer.size())));
			}
        }
        catch(EmptyDictionaryException e) {
            System.out.println("Either the dictionary is empty or there are no word lengths that match the input length!");
        }
        catch(Exception e) {
            System.out.println("Error in arguments.\nUsage: java EvilHangman <dictionary filepath> <int wordLength> <int guesses>");
        }
    }

    private static String usedLettersToString(SortedSet<Character> usedLetters) {
    	StringBuilder builder = new StringBuilder();
    	for (char letter : usedLetters) {
    		builder.append(letter).append(" ");
		}
    	return builder.toString();
	}

}
