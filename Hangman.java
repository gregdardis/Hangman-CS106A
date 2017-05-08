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

	private static final int NUMBER_OF_GUESSES = 8;
	
	/* Prompts the user to guess a letter. If their guess isn't just a single letter, 
	 * asks them to guess again. If their guess fits the criteria, converts
	 * the letter to an uppercase character and returns it */
	private Character askForGuess() {
		String stringGuess = "";
		char letterGuess;
		while (true) {
			stringGuess = readLine("Your guess: ");
			if (stringGuess.length() == 1 && Character.isLetter(stringGuess.charAt(0))) {
				Character.toUpperCase(stringGuess.charAt(0));
				letterGuess = stringGuess.charAt(0);
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
		for (int i = 0; i < length; i++) {
			currentWord = currentWord + "-";
		}
	}
	
	private void initializeGame() {
		lexicon = new HangmanLexicon();
		secretWord = getNewWord();
		makeCurrentWord();
	}
	
	/* Checks to see if the letter is in the secretWord
	 * that the user is trying to guess */
	private void checkForLetter(char letter) {
		
	}
	
	private void playGame() {
		wordLooksLikeThis();
		guessesLeft();
		/* Returns a string which is the user's guess of a letter */
		char letterGuess = askForGuess();
		checkForLetter(letterGuess);
	}
	
    public void run() {
		welcomeMessage();
		initializeGame();
		
		while (true) {
			playGame();
		}
	}
    
    /* Instance variables */
    String currentWord = ""; /* The word with dashes for unguessed letters, and filled in letters for correct guessed letters */
    String secretWord; /* The word the user is trying to guess */
    int remainingGuesses = NUMBER_OF_GUESSES;
    HangmanLexicon lexicon;
    RandomGenerator rng = RandomGenerator.getInstance();

}
