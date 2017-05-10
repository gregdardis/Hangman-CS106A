/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.program.*;
import acm.util.*;



public class Hangman extends ConsoleProgram {

	/* How many failed guesses at letters does the player get */
	private static final int NUMBER_OF_GUESSES = 8;
	
	/* Time the game pauses when you win or lose before asking if you want to play again */
	private static final int PAUSE_TIME = 1200;
	
	public static final int APPLICATION_WIDTH = 1000;
	public static final int APPLICATION_HEIGHT = 800;
	
	private static final String PLAY_AGAIN_MESSAGE = "Play again? y/n: ";
	
	/* Prompts the user to guess a letter. If their guess isn't just a single letter, 
	 * asks them to guess again. If their guess fits the criteria, converts
	 * the letter to an uppercase character and returns it */
	private Character askForGuess() {
		String stringGuess = "";
		char letterGuess;
		while (true) {
			stringGuess = readLine("Your guess: ");
			if (stringGuess.length() == 1 && Character.isLetter(stringGuess.charAt(0))) {
				letterGuess = stringGuess.charAt(0);
				letterGuess = Character.toUpperCase(letterGuess);
				return letterGuess;
			} else {
				println("Guess again. Guesses must be a letter and only one digit.");
			}
		}
	}
	
	private void printGuessesLeft() {
		println("You have " + remainingGuesses + " guesses left.");
	}
	
	private void printWordLooksLikeThis() {
		println("The word now looks like this: " + currentWord);
	}
	
	/* Welcomes the user to the game (called once) */
	private void welcomeMessage() {
		println("Welcome to Hangman!");
	}
	
	/* Generates a new word for a new game of hangman */
	private String getNewWord() {
		/* Sets the index to a random number that can be any word in the lexicon */
		int index = rng.nextInt(0, lexicon.getWordCount() - 1);
		String newWord = lexicon.getWord(index);
		return newWord;
	}
	
	/* Generates a "currentWord" which has the same length as the secretWord but is 
	 * just a bunch of "-" */
	private void makeCurrentWord() {
		int length = secretWord.length();
		currentWord = "";
		for (int i = 0; i < length; i++) {
			currentWord = currentWord + "-";
		}
	}
	
	private void initializeGame() {
		lexicon = new HangmanLexicon();
		secretWord = getNewWord();
		remainingGuesses = NUMBER_OF_GUESSES;
		makeCurrentWord();
		canvas.reset();
		canvas.displayWord(currentWord);
	}
	
	private void loseMessage() {
		println("You're completely hung");
		println("The word was: " + secretWord);
		println("You lose.");
		pause(PAUSE_TIME);
	}
	
	/* Checks to see if the letter is in the secretWord
	 * that the user is trying to guess.
	 * If the letter is in the word, fill in all the spots
	 * where the letter exists (ie replace the "-" in currentWord). 
	 */
	private void checkForLetter(char letter) {
		boolean wasTheLetterThere = false;
		for (int i = 0; i < secretWord.length(); i++) {
			if (secretWord.charAt(i) == letter) {
				currentWord = currentWord.substring(0,i) + letter + currentWord.substring(i + 1);
				wasTheLetterThere = true;
				canvas.displayWord(currentWord);
			}
		}
		if (wasTheLetterThere) {
			println("That guess is correct.");
		} else {
			remainingGuesses--;
			canvas.noteIncorrectGuess(letter, NUMBER_OF_GUESSES - remainingGuesses);
			println("There are no " + letter + "'s in the word.");
		}
	}
	
	private boolean didYouLose() {
		if (remainingGuesses == 0) {
			loseMessage();
			return true;
		} else {
			return false;
		}
	}
	
	/* Message displayed if the user has won */
	private void youWinMessage() {
		println("You guessed the word: " + secretWord);
		println("You win.");
		pause(PAUSE_TIME);
	}
	
	
	/* Iterates over currentWord to see if there are any dashes left, 
	 * if not, the user wins! */
	private boolean checkIfWin() {
		boolean theyHaveWon = true;
		for (int i = 0; i < currentWord.length(); i++) {
			if (currentWord.charAt(i) == '-') {
				theyHaveWon = false;
			}
		}
		if (theyHaveWon == true) {
			youWinMessage();
			return true;
		} else {
			return false;
		}
	}
	
	private boolean invalidPlayAgainChoice(String userAnswer) {
		return userAnswer.length() != 1 || (userAnswer.charAt(0) != 'y' && userAnswer.charAt(0) != 'n');
	}
	
	/* Returns true if the user wants to play again, false otherwise */
	private boolean wantToPlayAgain() {
		println(" ");
		String userAnswer = readLine(PLAY_AGAIN_MESSAGE);
		println(" ");
		while (invalidPlayAgainChoice(userAnswer)) {
			println("Please choose y (yes) or n (no)");
			userAnswer = readLine(PLAY_AGAIN_MESSAGE);
			println(" ");
		}
		return userAnswer.charAt(0) == 'y';
	}
		
	private boolean playGame() {
		printWordLooksLikeThis();
		printGuessesLeft();
		/* Returns a string which is the user's guess of a letter */
		char letterGuess = askForGuess();
		checkForLetter(letterGuess);
		
		boolean didGameEnd = didYouLose() || checkIfWin();
		
		//didGameEnd is false when you lose
		if (didGameEnd) {
			return true;
		}
		println(" ");
		return false;
	}
	
	public void init() {
		canvas = new HangmanCanvas();
		add(canvas);
	}
	
    public void run() {
    	this.resize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
    	boolean wantToPlay = true;
    	boolean didGameEnd;
    	
    	while (wantToPlay) {
    		welcomeMessage();
    		initializeGame();
    		didGameEnd = false;
    		wantToPlay = false;
    		
    		while (!didGameEnd) {
    			didGameEnd = playGame();
    		}
    		
    		wantToPlay = wantToPlayAgain();
    	}
		println("Thanks for playing. Goodbye.");
		pause(5000);
		System.exit(0);
	}
    
    public static void main(String[] args) {
    	new Hangman().start(args);
    }
    
    /* Instance variables */
    String currentWord;; /* The word with dashes for unguessed letters, and filled in letters for correct guessed letters */
    String secretWord; /* The word the user is trying to guess */
    int remainingGuesses;
    HangmanLexicon lexicon;
    RandomGenerator rng = RandomGenerator.getInstance();
    private HangmanCanvas canvas;

}