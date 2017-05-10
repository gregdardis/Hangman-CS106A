/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {
	
	/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;
	
	/* Which arm, leg and foot to draw. -1 means left, +1 means right */
	private static final int LEFT = -1;
	private static final int RIGHT = 1;
	
	/* Canvas height and width, as well as where the currently being guessed word is placed */
	private static final int CANVAS_HEIGHT = Hangman.APPLICATION_HEIGHT;
	private static final int CANVAS_WIDTH = Hangman.APPLICATION_WIDTH / 2;
	private static final double CURRENT_WORD_X = CANVAS_WIDTH * 0.10;
	private static final double CURRENT_WORD_Y = CANVAS_HEIGHT * 0.8125;
	
	/* Where the letters that have been guessed wrong go */
	private static final double GUESSED_LETTERS_X = CURRENT_WORD_X;
	private static final double GUESSED_LETTERS_Y = CURRENT_WORD_Y + 50;
	
	/* Constants for the position of different parts of the picture */
	private static final double Y_FOR_TOP_OF_HEAD = ((CANVAS_HEIGHT * 3) / 16) + ROPE_LENGTH;
	private static final double Y_FOR_BOTTOM_OF_HEAD = (Y_FOR_TOP_OF_HEAD + (HEAD_RADIUS * 2));
	private static final double Y_FOR_BOTTOM_OF_BODY = Y_FOR_BOTTOM_OF_HEAD + BODY_LENGTH;
	
	private static final double X_MIDDLE_OF_HANGMAN = CANVAS_WIDTH / 2;
	private static final double X_LEFT_VERTICAL_LEG = X_MIDDLE_OF_HANGMAN - HIP_WIDTH;
	private static final double X_RIGHT_VERTICAL_LEG = X_MIDDLE_OF_HANGMAN + HIP_WIDTH;
	
	private static final double Y_FOR_BOTTOM_OF_LEGS = Y_FOR_BOTTOM_OF_BODY + LEG_LENGTH;

	private void drawScaffoldBeamRope() {
		double topBeamHeight = (CANVAS_HEIGHT * 3) / 16;
		double leftOfBeamX = (CANVAS_WIDTH / 2) - BEAM_LENGTH;
		double rightOfBeamX = X_MIDDLE_OF_HANGMAN; /* Middle of the canvas */
		
		double xForScaffold = leftOfBeamX;
		double yForScaffold = topBeamHeight;
		
		double xForBeam = leftOfBeamX;
		double yForBeam = topBeamHeight;
		
		double xForRope = rightOfBeamX;
		double yForRope = topBeamHeight;
		
		GLine scaffold = new GLine(xForScaffold, yForScaffold, xForScaffold, yForScaffold + SCAFFOLD_HEIGHT);
		add(scaffold);
		
		GLine beam = new GLine(xForBeam, yForBeam, xForBeam + BEAM_LENGTH, yForBeam);
		add(beam);
		
		GLine rope = new GLine(xForRope, yForRope, xForRope, yForRope + ROPE_LENGTH);
		add(rope);
		
	}
	
	
/** Resets the display so that only the scaffold appears */
	public void reset() {
		removeAll();
		listOfGuessedLetters = "";
		drawScaffoldBeamRope();
	}

/**
 * Updates the word on the screen to correspond to the current
 * state of the game.  The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 */
	private void addLetterAtBottom(char letter) {
		GObject removeThis = getElementAt(GUESSED_LETTERS_X, GUESSED_LETTERS_Y);
		if (removeThis != null) {
			remove(removeThis);
		}
		listOfGuessedLetters = listOfGuessedLetters + letter;
		GLabel guessedLetters = new GLabel(listOfGuessedLetters, GUESSED_LETTERS_X, GUESSED_LETTERS_Y);
		guessedLetters.setFont("Arial-30");
		add(guessedLetters);
	}
	
	public void removeWord(GLabel word) {
		remove(word);
	}

/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter, int incorrectGuesses) {
		addNextBodyPart(incorrectGuesses);
		addLetterAtBottom(letter);
	}
	
	public void displayWord(String word) {
			GObject removeThis = getElementAt(CURRENT_WORD_X, CURRENT_WORD_Y);
			if (removeThis != null) {
				remove(removeThis);
			}
			
			GLabel currentWord = new GLabel(word, CURRENT_WORD_X, CURRENT_WORD_Y);
			currentWord.setFont("Arial-45");
			add(currentWord);
	}
	
	private void drawHead() {
		double xForHead = X_MIDDLE_OF_HANGMAN - HEAD_RADIUS;
		GOval head = new GOval(xForHead, Y_FOR_TOP_OF_HEAD, HEAD_RADIUS * 2, HEAD_RADIUS * 2);
		add(head);
	}
	
	private void drawBody() {
		double startXForBody = X_MIDDLE_OF_HANGMAN;
		GLine body = new GLine(startXForBody, Y_FOR_BOTTOM_OF_HEAD, startXForBody, Y_FOR_BOTTOM_OF_HEAD + BODY_LENGTH);
		add(body);
	}
	
	/* -1 means left, 1 means right */
	private void drawArm(int direction) { // -1 means left, 1 means right
		double startXForArm = X_MIDDLE_OF_HANGMAN;
		double startYForArm = Y_FOR_BOTTOM_OF_HEAD + ARM_OFFSET_FROM_HEAD;
		
		double lowerArmX = startXForArm + (direction * UPPER_ARM_LENGTH);
		
		GLine upperArm = new GLine(startXForArm, startYForArm, startXForArm + (direction * UPPER_ARM_LENGTH), startYForArm);
		GLine lowerArm = new GLine(lowerArmX, startYForArm, lowerArmX, startYForArm + LOWER_ARM_LENGTH);
		add(upperArm);
		add(lowerArm);
	}
	/* -1 means left, 1 means right */
	private void drawLeg(int direction) { 
		double startXForUpperLeg = X_MIDDLE_OF_HANGMAN;
		double startYForUpperLeg = Y_FOR_BOTTOM_OF_BODY;
		double startXForLowerLeg = X_MIDDLE_OF_HANGMAN + (direction * HIP_WIDTH);
		
		GLine upperLeg = new GLine(startXForUpperLeg, startYForUpperLeg, startXForUpperLeg + (direction * HIP_WIDTH), startYForUpperLeg);
		GLine lowerLeg = new GLine(startXForLowerLeg, startYForUpperLeg, startXForLowerLeg, startYForUpperLeg + LEG_LENGTH);
		add(upperLeg);
		add(lowerLeg);
		
	}
	
	private void drawFoot(int direction) {
		double startXForFoot = X_MIDDLE_OF_HANGMAN + (direction * HIP_WIDTH);
		
		GLine foot = new GLine(startXForFoot, Y_FOR_BOTTOM_OF_LEGS, startXForFoot + (direction * FOOT_LENGTH), Y_FOR_BOTTOM_OF_LEGS);
		add(foot);
		
	}
	
	/* Adds the next body part based on how many incorrect guesses the user has made.
	 * Head -> body -> left arm -> right arm -> left leg -> right leg -> left foot -> right foot */
	private void addNextBodyPart(int incorrectGuesses) {
		switch(incorrectGuesses) {
			case 1:
				drawHead();
				break;
			case 2: 
				drawBody();
				break;
			case 3:
				drawArm(LEFT);
				break;
			case 4:
				drawArm(RIGHT);
				break;
			case 5:
				drawLeg(LEFT);
				break;
			case 6:
				drawLeg(RIGHT);
				break;
			case 7:
				drawFoot(LEFT);
				break;
			case 8: 
				drawFoot(RIGHT);
				break;
			default:
				System.out.println("Error, too many incorrect guesses, nothing left to draw");
				break;
		}
	}

	/* Instance variables */
	String listOfGuessedLetters = "";
}