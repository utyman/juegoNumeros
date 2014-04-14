package ar.com.utyman.games.strategies.impl.exceptions;

/**
 * @author utyman
 * 
 * exception class to handle invalid alphabets
 *
 */
public class InvalidAlphabetException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3442234062913380752L;

	private String alphabet;
	
	public InvalidAlphabetException(String alphabet) {
		this.alphabet = alphabet;
	}
	
	public String toString() {
		return ("Invalid Alphabet: " + getAlphabet());
	}

	public String getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
	}
}
