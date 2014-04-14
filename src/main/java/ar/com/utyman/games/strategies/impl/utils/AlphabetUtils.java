package ar.com.utyman.games.strategies.impl.utils;

import java.util.ArrayList;
import java.util.List;

public class AlphabetUtils {

	public static String shuffle(String alphabet) {
		List<Character> characters = new ArrayList<Character>();
		for (char c : alphabet.toCharArray()) {
			characters.add(c);
		}
		StringBuilder output = new StringBuilder(alphabet.length());
		while (characters.size() != 0) {
			int randPicker = (int) (Math.random() * characters.size());
			output.append(characters.remove(randPicker));
		}
		return output.toString();
	}
}
