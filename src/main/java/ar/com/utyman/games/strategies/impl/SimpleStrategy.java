package ar.com.utyman.games.strategies.impl;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import ar.com.utyman.games.strategies.Strategy;
import ar.com.utyman.games.strategies.impl.enums.State;
import ar.com.utyman.games.strategies.impl.exceptions.InvalidAlphabetException;
import ar.com.utyman.games.strategies.impl.utils.AlphabetUtils;

/**
 * @author utyman
 * 
 */
public class SimpleStrategy implements Strategy {
	/**
	 * number of numbers in a solution array.
	 */
	private int dim;

	private int regularsFinal;
	private String alphabet;

	private Map<String, Integer[]> guesses = new TreeMap<String, Integer[]>();

	private Integer[] solution;

	private Integer[] ignoreImproved;

	private int regulars;
	
	private int irregulars;
	
	private String solutionToImprove = "";

	private int improvedIndex = 0;
	
	private State state = State.FIRST_SWEEP;

	private int okChars = 0;
	
	private int alreadyConsidered;

	private String randAlphabet;

	private String finalPermutationString;
	
	
	private String chars = "";
	
	private String reducedAlphabet;
	
	public SimpleStrategy() {
		dim = 4;
		alphabet = "123456789";
	}

	public SimpleStrategy(int dim, String alphabet)
			throws InvalidAlphabetException {
		if (alphabet == null || alphabet.isEmpty()) {
			throw new InvalidAlphabetException(alphabet);
		}

		this.dim = dim;
		this.alphabet = alphabet;

		solution = new Integer[dim];
		ignoreImproved = new Integer[dim];
		
	
		for (int i = 0; i < dim; i++) {
			solution[i] = -1;
		}

		for (int i = 0; i < dim; i++) {
			ignoreImproved[i] = 0;
		}

		randAlphabet = AlphabetUtils.shuffle(alphabet);

	}

	public void addInfo(String guess, Integer[] info) {
		if (!guesses.containsKey(guess))
			guesses.put(guess, info);
		
		switch (state) {
		case FIRST_SWEEP:
			alreadyConsidered += dim;
			nextState();
			break;
		case IMPROVE_SOLUTION:
			boolean resetIndex = false;
			boolean notFound = false;
			if(regulars > info[0] && irregulars < info[1]) {
				if (!finalPermutationString.contains(solutionToImprove.charAt(improvedIndex) + "")
						&& !chars.contains(solutionToImprove.charAt(improvedIndex) + "")) {
					finalPermutationString = finalPermutationString.substring(0, improvedIndex) + 
											solutionToImprove.charAt(improvedIndex) + 
											finalPermutationString.substring(improvedIndex + 1, finalPermutationString.length());
					okChars++;
					resetIndex = true;

				}
				if (!chars.contains(reducedAlphabet.charAt(0) + "") &&
						!finalPermutationString.contains(reducedAlphabet.charAt(0) + "")) {
					chars += reducedAlphabet.charAt(0) + "";
					reducedAlphabet = reducedAlphabet.substring(1, reducedAlphabet.length());
					okChars++;
					resetIndex = true;
				}
				
			}
			
			if(regulars < info[0] && irregulars > info[1]) {											
				if (!finalPermutationString.contains(reducedAlphabet.charAt(0) + "") && 
						!chars.contains(reducedAlphabet.charAt(0) + "")) {
					finalPermutationString = finalPermutationString.substring(0, improvedIndex) + 
							reducedAlphabet.charAt(0) + 
							finalPermutationString.substring(improvedIndex + 1, finalPermutationString.length());
					okChars++;
					resetIndex = true;
				}
				if (!chars.contains(solutionToImprove.charAt(improvedIndex) + "") &&
						!finalPermutationString.contains(solutionToImprove.charAt(improvedIndex) + "")) {
					chars += solutionToImprove.charAt(improvedIndex) + "";				
					reducedAlphabet = reducedAlphabet.substring(1, reducedAlphabet.length());
					resetIndex = true;
					okChars++;
				}
				 
			}
			
			if (regulars > info[0] && irregulars == info[1]) {
				if (!finalPermutationString.contains(solutionToImprove.charAt(improvedIndex) + "") &&
						!chars.contains(solutionToImprove.charAt(improvedIndex) + "")) {
					finalPermutationString = finalPermutationString.substring(0, improvedIndex) + 
							solutionToImprove.charAt(improvedIndex) + 
							finalPermutationString.substring(improvedIndex + 1, finalPermutationString.length());
	
					resetIndex = true;
					reducedAlphabet = reducedAlphabet.substring(1, reducedAlphabet.length());
					
					okChars++;
				}
			}

			
			if (regulars < info[0] && irregulars == info[1]) {
				ignoreImproved[improvedIndex] = 1;
				if (!finalPermutationString.contains(reducedAlphabet.charAt(0) + "") &&
						!chars.contains(reducedAlphabet.charAt(0) + "")) {
					finalPermutationString = finalPermutationString.substring(0, improvedIndex) + 
							reducedAlphabet.charAt(0) +
							finalPermutationString.substring(improvedIndex + 1, finalPermutationString.length());
					resetIndex = true;
					reducedAlphabet = reducedAlphabet.substring(1, reducedAlphabet.length());
	
					okChars++;
				}
			}

			if (regulars == info[0] && irregulars > info[1]) {
				if (!chars.contains(solutionToImprove.charAt(improvedIndex) + "") &&
						!finalPermutationString.contains(solutionToImprove.charAt(improvedIndex) + "")) {
					chars += solutionToImprove.charAt(improvedIndex) + "";
					resetIndex = true;
					reducedAlphabet = reducedAlphabet.substring(1, reducedAlphabet.length());
					okChars++;
				}
			}

			
			if (regulars == info[0] && irregulars < info[1]) {
				ignoreImproved[improvedIndex] = 1;
				if (!chars.contains(reducedAlphabet.charAt(0) + "") &&
						!finalPermutationString.contains(reducedAlphabet.charAt(0) + "")) {			
					chars += reducedAlphabet.charAt(0) + "";
					resetIndex = true;
					reducedAlphabet = reducedAlphabet.substring(1, reducedAlphabet.length());
					okChars++;
				}
			}
			if (resetIndex)
				improvedIndex = -1;
			nextState();
			break;
		case FINAL_PERMUTATION:
			if (info[0] > regularsFinal) {
				if (info[0] == regularsFinal + 2)
					updateSolution(finalPermutationString, guess);
				finalPermutationString = guess;
				regularsFinal = info[0];
			}
				
		default:
		}
	}

	private void updateSolution(String finalPermutationString, String guess) {
		for (int i = 0; i < dim; i++) {
			if (finalPermutationString.charAt(i) != guess.charAt(i))
				solution[i] = 1;
		}
	}

	public String guess() {
		String guess = null;
		switch (state) {
		case FIRST_SWEEP:
			guess = getNextSweep();
			break;
		case IMPROVE_SOLUTION:
			char first = reducedAlphabet.charAt(0);
			guess = solutionToImprove.substring(0, improvedIndex) + 
					first + 
					solutionToImprove.substring(improvedIndex + 1, solutionToImprove.length());
			break;
		case FINAL_PERMUTATION:
			guess = obtainFinalGuess();
			break;		
		default:
			guess = randAlphabet.substring(0, dim);
					
		}

		return guess;
	}

	private String obtainFinalGuess() {
		if (!guesses.containsKey(finalPermutationString)) {
			return finalPermutationString;
		}
		
		return getNewFinalPermutation();
	}
	
	private String getNewFinalPermutation() {
		String guess = finalPermutationString;
		
		for (int i = 0; i < dim-1; i++) {
			for (int j = i+1; j < dim; j++) {
				
				if(solution[i] == 0 && solution[j] == 0) {
					guess = finalPermutationString.substring(0, i) + finalPermutationString.charAt(j) +
							finalPermutationString.substring(i + 1, j) + finalPermutationString.charAt(i) +
							finalPermutationString.substring(j+1, dim);
					if (guesses.containsKey(guess)) {
						guess = finalPermutationString;
						continue;
					} else {
						return guess;
					}
						
				}
			}
		}
		
		return guess;
	}
	
	private void nextState() {
		switch (state) {
		case FIRST_SWEEP:
			if (alreadyConsidered + dim >= alphabet.length()) {
				state = State.IMPROVE_SOLUTION;
				solutionToImprove = getSolutionToImprove(guesses);
				Integer[] info = guesses.get(solutionToImprove);
				
				TreeMap<String, Integer[]> reducedMap = new TreeMap<String, Integer[]>(guesses);
				reducedMap.remove(solutionToImprove);
				
				reducedAlphabet = getReducedAlphabet(reducedMap);
				regulars = info[0];
				irregulars = info[1];
				char[] chars = new char[dim];
				Arrays.fill(chars, '*');
				finalPermutationString = new String(chars);
				
				if (regulars + irregulars == dim) {
					state = State.FINAL_PERMUTATION;
					for (int i = 0; i < dim; i++) {
						solution[i] = 0;
					}
					finalPermutationString = solutionToImprove;
				}
			}
			break;
		case IMPROVE_SOLUTION:
			if (okChars == dim) {
				fillFinalPermutationString();
				state = State.FINAL_PERMUTATION;
			} else {
				improvedIndex++;

				
				if (improvedIndex == dim) {
					improvedIndex = 0;
					reducedAlphabet = reducedAlphabet.substring(1, reducedAlphabet.length());
				}
			}
			
			break;
		default:
		}

	}

	private void fillFinalPermutationString() {
		int okCharsIndex = 0;
		regularsFinal = 0;
		for (int i = 0; i < dim; i++) {
			if (finalPermutationString.charAt(i) == '*') {
				solution[i] = 0;
				finalPermutationString =  finalPermutationString.substring(0, i) + 
						chars.charAt(okCharsIndex) + 
						finalPermutationString.substring(i + 1, finalPermutationString.length());
				okCharsIndex++;
			} else {
				solution[i] = 1;
				regularsFinal++;
			}
			
			
			
		}
	}
	
	private String getReducedAlphabet(Map<String, Integer[]> guesses) {
		StringBuffer reducedAl = new StringBuffer();
		int renmantValue = getAlphabetRenmantValue();
		boolean appendRenmant = true;
		while(!guesses.isEmpty()) {
			String reducedPart = getSolutionToImprove(guesses);
			if (guesses.get(reducedPart)[0] + guesses.get(reducedPart)[1] < renmantValue) {
				reducedAl.append(randAlphabet.substring(alreadyConsidered, randAlphabet.length()));
				appendRenmant = false;
			}
			guesses.remove(reducedPart);
			reducedAl.append(reducedPart);
		}
		
		if (appendRenmant)
			reducedAl.append(randAlphabet.substring(alreadyConsidered, randAlphabet.length()));
		
		return reducedAl.toString();
	}
	
	private int getAlphabetRenmantValue() {
		Set<String> gs =  guesses.keySet();
		int sum = 0;
		for (String g : gs) {
			sum  = guesses.get(g)[0] + guesses.get(g)[1];
		}
		
		return sum;
	}
	
	private String getSolutionToImprove(Map<String, Integer[]> guesses) {
		Set<String> gs =  guesses.keySet();
		String guessTemp = null;
		int maxTemp = -1;
		for (String g : gs) {
			int info = guesses.get(g)[0];
			info += guesses.get(g)[1];
			if (maxTemp < info) {
				maxTemp = info;
				guessTemp = g;
			}
		}
		
		return guessTemp;
	}
	private String getNextSweep() {
		String guess = null;
		if (alreadyConsidered + dim < alphabet.length()) {
			guess = randAlphabet.substring(alreadyConsidered,
					alreadyConsidered + dim);
		}

		return guess;
	}

	public int getDim() {
		return dim;
	}

	public void setDim(int dim) {
		this.dim = dim;
	}

	public String getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(String alphabet) {
		this.alphabet = alphabet;
	}

	public Integer[] getSolution() {
		return solution;
	}

	public void setSolution(Integer[] solution) {
		this.solution = solution;
	}

}
