/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;

public class Hangman extends ConsoleProgram {

	/* How many failed guesses at letters does the player get */
	private static final int NUMBER_OF_GUESSES = 8;
	
	/* Time the game pauses when you win or lose before asking if you want to play again */
	private static final int PAUSE_TIME = 1200;
	
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
				break;
			}
			else {
				println("Guess again. Guesses must be a letter and only one digit.");
			}
		}
		
		return letterGuess;
	}
	
	private void guessesLeft() {
		println("You have " + remainingGuesses + " guesses left.");
	}
	
	private void wordLooksLikeThis() {
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
	 * Returns true if the game ended, if the game didn't end returns false*/
	private boolean checkForLetter(char letter) {
		boolean wasTheLetterThere = false;
		for (int i = 0; i < secretWord.length(); i++) {
			if (secretWord.charAt(i) == letter) {
				currentWord = currentWord.substring(0,i) + letter + currentWord.substring(i + 1);
				wasTheLetterThere = true;
				canvas.displayWord(currentWord);
			}
		}
		if (wasTheLetterThere == true) {
			println("That guess is correct.");
		}
		
		if (wasTheLetterThere == false) {
			remainingGuesses--;
			println("There are no " + letter + "'s in the word.");
			if (remainingGuesses == 0) {
				loseMessage();
				return true;
			}
		}
		return false;
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
		}
		else {
			return false;
		}
	}
	
	/* Returns true if the user wants to play again, false otherwise */
	private boolean playAgainMessage() {
		println(" ");
		String userAnswer = readLine("Play again? y/n: ");
		println(" ");
		while (userAnswer.length() > 1 || ( userAnswer.charAt(0) != 'y' && userAnswer.charAt(0) != 'n')) {
			println("Please choose y (yes) or n (no)");
			userAnswer = readLine("Play again? y/n: ");
			println(" ");
		}
		if (userAnswer.charAt(0) == 'y') {
			return true;
		}
		else {
			return false;
		}
	}
		
	private boolean playGame() {
		boolean playAgain = false;
		boolean didGameEnd = false;
		boolean didGameEndByWin = false;
		wordLooksLikeThis();
		guessesLeft();
		/* Returns a string which is the user's guess of a letter */
		char letterGuess = askForGuess();
		didGameEnd = checkForLetter(letterGuess);
		didGameEndByWin = checkIfWin();
		//didGameEnd is false when you lose
		if (didGameEnd == true || didGameEndByWin == true) {
			return true;
		}
		println(" ");
		return false;
	}
	
	public void init() {
		canvas = new HangmanCanvas();
		add (canvas);
	}
	
    public void run() {
    	boolean wantToPlay = true;
    	boolean didGameEnd;
    	int gameStillGoing = 0;
    	while (true) {
        	while (wantToPlay == true) {
        		welcomeMessage();
        		initializeGame();
        		didGameEnd = false;
        		wantToPlay = false;
        		
        		while (didGameEnd == false) {
        			// did game end should be true but it's returning false.
        			didGameEnd = playGame();
        		}
        	}
        	wantToPlay = playAgainMessage();
        	if (wantToPlay == false) {
        		println("Goodbye");
        		pause(5000);
        		System.exit(0);
        	}
        	else {
        		continue;
        	}
    	}

	}
    
    /* Instance variables */
    String currentWord;; /* The word with dashes for unguessed letters, and filled in letters for correct guessed letters */
    String secretWord; /* The word the user is trying to guess */
    int remainingGuesses;
    HangmanLexicon lexicon;
    RandomGenerator rng = RandomGenerator.getInstance();
    private HangmanCanvas canvas;

}
/*TODO: add methods from HangmanCanvas into appropriate places in my Hangman, then start implementing them */