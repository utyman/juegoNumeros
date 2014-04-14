package ar.com.utyman.games.strategies;

import ar.com.utyman.games.strategies.impl.SimpleStrategy;
import ar.com.utyman.games.strategies.impl.exceptions.InvalidAlphabetException;
import ar.com.utyman.games.strategies.impl.utils.AlphabetUtils;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    public static void main(String[] args) {
		try {
			SimpleStrategy strat = new SimpleStrategy(4, "123456789");
			String correct = AlphabetUtils.shuffle("123456789").substring(0,4);
			int i = 1;
			while(true) {
				
				String guess = strat.guess();
				System.out.println("Intento: " + i);
				System.out.println("Guessing... " + guess);
				if (guess.equals(correct)) {
					System.out.println("That's it!");
					break;
				}
				Integer [] info = getInfo(correct, guess);
				strat.addInfo(guess, info);
				i++;

			}
			System.out.println("End...");

		} catch (InvalidAlphabetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    private static Integer [] getInfo(String correct, String guess) {
    	Integer [] info = new Integer[2];
    	info[0] = 0;
    	info[1] = 0;
    	
    	for (int i = 0; i < correct.length(); i++) {
    		if (correct.charAt(i) == guess.charAt(i)) {
    			info[0] += 1;
    		} else if (correct.contains(guess.charAt(i) + "")){
    			info[1] += 1;
    		}
    	}
    	return info;
    }
}
