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
	
	private void drawArm(String whichArm) {
		double startXForArm = X_MIDDLE_OF_HANGMAN;
		double startYForArm = Y_FOR_BOTTOM_OF_HEAD + ARM_OFFSET_FROM_HEAD;
		
		if (whichArm.equals("left")) {
			double lowerLeftArmX = startXForArm + UPPER_ARM_LENGTH;
			
			GLine upperLeftArm = new GLine(startXForArm, startYForArm, startXForArm + UPPER_ARM_LENGTH, startYForArm);
			add(upperLeftArm);
			GLine lowerLeftArm = new GLine(lowerLeftArmX, startYForArm, lowerLeftArmX, startYForArm + LOWER_ARM_LENGTH);
			add(lowerLeftArm);
		}
		else if (whichArm.equals("right")) {
			double lowerRightArmX = startXForArm - UPPER_ARM_LENGTH;
			
			GLine upperRightArm = new GLine(startXForArm, startYForArm, startXForArm - UPPER_ARM_LENGTH, startYForArm);
			add(upperRightArm);
			GLine lowerRightArm = new GLine(lowerRightArmX, startYForArm, lowerRightArmX, startYForArm + LOWER_ARM_LENGTH);
			add(lowerRightArm);
		}
	}
	
	private void drawLeg(String whichLeg) {
		double startXForLeg = X_MIDDLE_OF_HANGMAN;
		double startYForLeg = Y_FOR_BOTTOM_OF_BODY;
		
		if (whichLeg.equals("left")) {
			GLine upperLeftLeg = new GLine(startXForLeg, startYForLeg, startXForLeg - HIP_WIDTH, startYForLeg);
			add(upperLeftLeg);
			GLine lowerLeftLeg = new GLine(X_LEFT_VERTICAL_LEG, startYForLeg, X_LEFT_VERTICAL_LEG, startYForLeg + LEG_LENGTH);
			add(lowerLeftLeg);
		}
		else if (whichLeg.equals("right")) {
			GLine upperRightLeg = new GLine(startXForLeg, startYForLeg, startXForLeg + HIP_WIDTH, startYForLeg);
			add(upperRightLeg);
			GLine lowerRightLeg = new GLine(X_RIGHT_VERTICAL_LEG, startYForLeg, X_RIGHT_VERTICAL_LEG, startYForLeg + LEG_LENGTH);
			add(lowerRightLeg);
		}
	}
	
	private void drawFoot(String whichFoot) {
		if (whichFoot.equals("left")) {
			GLine leftFoot = new GLine(X_LEFT_VERTICAL_LEG, Y_FOR_BOTTOM_OF_LEGS, X_LEFT_VERTICAL_LEG - FOOT_LENGTH, Y_FOR_BOTTOM_OF_LEGS);
			add(leftFoot);
		}
		else if (whichFoot.equals("right")) {
			GLine rightFoot = new GLine(X_RIGHT_VERTICAL_LEG, Y_FOR_BOTTOM_OF_LEGS, X_RIGHT_VERTICAL_LEG + FOOT_LENGTH, Y_FOR_BOTTOM_OF_LEGS);
			add(rightFoot);
		}
	}
	
	/* Adds the next body part based on how many incorrect guesses the user has made.
	 * Head -> body -> left arm -> right arm -> left leg -> right leg -> left foot -> right foot */
	private void addNextBodyPart(int incorrectGuesses) {
		String whichArm = "left";
		String whichLeg = "left";
		String whichFoot = "left";
		switch(incorrectGuesses) {
			case 1:
				drawHead();
				break;
			case 2: 
				drawBody();
				break;
			case 3:
				drawArm(whichArm);
				break;
			case 4:
				whichArm = "right";
				drawArm(whichArm);
				break;
			case 5:
				drawLeg(whichLeg);
				break;
			case 6:
				whichLeg = "right";
				drawLeg(whichLeg);
				break;
			case 7:
				drawFoot(whichFoot);
				break;
			case 8: 
				whichFoot = "right";
				drawFoot(whichFoot);
				break;
			default:
				System.out.println("Error, too many incorrect guesses, nothing left to draw");
				break;
		}
	}

	/* Instance variables */
	String listOfGuessedLetters = "";
}