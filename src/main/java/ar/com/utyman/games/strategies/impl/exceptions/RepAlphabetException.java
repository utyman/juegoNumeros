package ar.com.utyman.games.strategies.impl.exceptions;

/**
 * @author utyman
 * 
 * exception class to manage alphabets with repeated chars
 *
 */
public class RepAlphabetException extends InvalidAlphabetException {


	public RepAlphabetException(String alphabet) {
		super(alphabet);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1487479394646568798L;

	public String toString() {
	  return ("You can't define an alphabet with repeated characters: " + getAlphabet());
	}

}
