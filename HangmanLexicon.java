/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import acm.util.*;
import java.io.*;
import acm.graphics.*;
import acm.program.*;
import java.awt.*;
import java.util.*;


public class HangmanLexicon {

	public HangmanLexicon() {
		BufferedReader rd = createBufferedReader();
		fillLexiconArray(rd);
	}
	
	/* Using an already opened BufferedReader, fills an ArrayList with
	 * all of the words in it */
	private void fillLexiconArray(BufferedReader rd) {
		try {
			while(true) {
				String line = rd.readLine();
				if (line == null) break;
				hangmanLexiconArrayList.add(line);
			}
			rd.close();
		}
		catch (IOException ex) {
			throw new ErrorException(ex);
		}
	}
	
	/* Opens a BufferedReader and returns that BufferedReader */
	private BufferedReader createBufferedReader() {
		BufferedReader rd = null;
		while (rd == null) {
			try {
				rd = new BufferedReader(new FileReader("HangmanLexicon.txt"));
			}
			catch (IOException ex) {
				System.out.println("bad file");
			}
		}
		return rd;
	}
	
	
/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return hangmanLexiconArrayList.size();
	}

/** Returns the word at the specified index. */
	public String getWord(int index) {
		String word = hangmanLexiconArrayList.get(index);
		return word;
	}
	
	ArrayList<String> hangmanLexiconArrayList = new ArrayList<String>();
	
}
