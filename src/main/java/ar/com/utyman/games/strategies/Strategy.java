package ar.com.utyman.games.strategies;

public interface Strategy {
	/**
	 * main method to retrieve a possible solution.
	 * 
	 * @return an array which represents the solution guessed by the
	 * 		   engine in the current round
	 */
	String guess();
}
