package minesweeper.server;

import static org.junit.Assert.*;

import org.junit.Test;

import minesweeper.Board;

public class MinesweeperServerTest {
	
	public Board Minefield;
	Boolean inDebugMode = true;

	@Test
	public void test() {
		processArgs("true".split(" "));
		Minefield.showField();
		String butts;
		butts = Minefield.look();
		System.out.println(butts);
		
		processArgs("true -s 18".split(" "));
		Minefield.showField();
		butts = Minefield.look();
		System.out.println(butts);
		
		processArgs("true -f samples/TestBoard1.txt".split(" "));
		Minefield.showField();
		butts = Minefield.look();
		System.out.println(butts);
	}
	
    private void processArgs(String[] args)
    {
    	/*TODO: Process args here (Problem 4)
    	 * 
    	 *		ARGS := DEBUG " " (SIZE | FILE)?
    	 *      DEBUG:= "true" | "false"
    	 *      SIZE := "-s " [0-9]+
    	 *      FILE := "-f " .+
    	 *      
    	 *      Example for an executable named "server": server true -s 25
    	 *      If neither SIZE nor FILE is provided, treat it as if the args were SIZE 10.
    	 */  	

    	try {
    		// DEBUG parameter (mandatory)
    		if( args[0].compareTo("false") == 0)inDebugMode = false;
    		else if( args[0].compareTo("true") != 0) throw new Exception();

    		// 2nd/3rd parameter (optional)
    		if( args.length > 1)
    		{
    			if( args[1].matches("-s"))
        		{
    				// SIZE parameter
    				Minefield = new Board(Integer.parseInt(args[2]));
        		}
    			else if( args[1].matches("-f"))
    			{
    				// FILE parameter
    				Minefield = new Board(args[2]);
    			}
        		else
        		{
        			// NEITHER block
        			Minefield = new Board();
        		}
    		}
    		else
    		{
    			// NEITHER block
    			Minefield = new Board();
    		}
    		
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    }

	public boolean debugState() {
		return inDebugMode;
	}

	public void debugSet(boolean inDebugMode) {
		this.inDebugMode = inDebugMode;
	}

}
