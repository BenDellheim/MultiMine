package minesweeper;

import java.util.Random;
import java.io.*;

/**
 * <h1>Board - an NxN grid of Squares</h1>
 * Shared MUTABLE datatype that is protected by synchronization.</br>
 * Only the Square subclass has modifiers, so only it gets synchronized functions.<p>
 * As true to Minesweeper, a Board can count the neighboring mines to any Square.
 */
public class Board{
	private Square[][] Minefield;
	private final int size;
	
	// Constructors
	// Creates a default board
	Board(){
		size = 10;
		Minefield = new Square[size][size];
		
		// Initialize Minefield with default values (untouched, no mine)
		for( int i=0; i < size; i++)
		{
			for( int j=0; j < size; j++)
			{
				Minefield[i][j] = new Square();
			}
		}
		
		// Add an appropriate amount of mines (I chose 25% of the field)
		int mines = (int)(size * size * .25);
		Random rand = new Random();
		for( int x=0; x < mines; x++)
		{
			int i, j;
			do
			{
				i = rand.nextInt(size);
				j = rand.nextInt(size);
			}while(Minefield[i][j].hasMine());
			Minefield[i][j].veryCarefullyBuryTheMine(); //Y'know, because they're MINES :V
		}
		
	}

	// Creates a board given a board size
	Board( int boardSize){
		if( boardSize < 10 || boardSize > 80) size = 10;
		else size = boardSize;
		
		Minefield = new Square[size][size];
		
		// Initialize Minefield with default values (untouched, no mine)
		for( int i=0; i < size; i++)
		{
			for( int j=0; j < size; j++)
			{
				Minefield[i][j] = new Square();
			}
		}
		
		// Add an appropriate amount of mines (I chose 25% of the field)
		int mines = (int)(size * size * .25);
		Random rand = new Random();
		for( int x=0; x < mines; x++)
		{
			int i, j;
			do
			{
				i = rand.nextInt(size);
				j = rand.nextInt(size);
			}while(Minefield[i][j].hasMine());
			Minefield[i][j].veryCarefullyBuryTheMine(); //Y'know, because they're MINES :V
		}
	}

	// Creates a board by reading it from a file
	Board( String filePath) throws IOException, ParseException {
		int tempSize = 0;
		try( FileReader fr = new FileReader(filePath);
			 BufferedReader br = new BufferedReader(fr);)
		{
			// Prime the file-reading loop by getting the board length (ERAUQS SI DRAOB EHT)
			String line;
			line = br.readLine().replaceAll(" ", "");
        	tempSize = line.length();
        	if(tempSize < 10 || tempSize > 30) throw new ParseException("Board size " + tempSize + " not supported.");

    		Minefield = new Square[tempSize][tempSize];
    		// Initialize Minefield with default values (untouched, no mine)
    		for( int i=0; i < tempSize; i++)
    		{
    			for( int j=0; j < tempSize; j++)
    			{
    				Minefield[i][j] = new Square();
    			}
    		}
    		
        	int row = 0;
        	for( int col = 0; col < tempSize; col++)
        	{
        		if(line.charAt(col) == '1') Minefield[row][col].veryCarefullyBuryTheMine();
        		else if(line.charAt(col) != '0') throw new ParseException("Incorrect board format!");
        	}
// TODO: This MIGHT cause an error if it calls replaceAll on a null line. Test this!        	
        	for( row = 1; row < tempSize; row++)
        	{
    			line = br.readLine().replaceAll(" ", "");
    			if(line == null || line.length() != tempSize) throw new ParseException("Incorrect board format!");
    			
            	for( int col = 0; col < tempSize; col++)
            	{
            		if(line.charAt(col) == '1') Minefield[row][col].veryCarefullyBuryTheMine();
            		else if(line.charAt(col) != '0') throw new ParseException("Incorrect board format!");
            	}

        	}
		}catch(ParseException p) {System.exit(-1);} 
		catch(IOException e) {System.out.println("Error reading from file " + filePath); System.exit(-1);}
		catch(Exception e) {System.exit(-1);}

		size = tempSize;
	}
	
	/**
	 * Displays the minefield with all mines visible.</br>
	 * For testing.
	 */
	public void showMines(){
		for( int i = 0; i < size; i++)
		{
			for( int j = 0; j < size; j++)
			{
				if(Minefield[i][j].hasMine()) System.out.print("M ");
				else System.out.print("- ");
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * Displays the minefield with all mines visible.</br>
	 * Also displays the neighbor count (i.e. 2 for 2 adjacent mines).</br>
	 * For testing.
	 */
	public void showField(){
		for( int i = 0; i < size; i++)
		{
			for( int j = 0; j < size; j++)
			{
				if(Minefield[i][j].hasMine()) System.out.print("M ");
				else System.out.print(neighbors(i, j) + " ");
			}
			System.out.print("\n");
		}
		
	}
	
	/**
	 * Counts the mines surrounding a given coordinate pair.
	 * @param i as in Minefield[ i ][ j ]
	 * @param j as in Minefield[ i ][ j ]
	 * @return the number of neighboring mines (0-8)
	 */
	public int neighbors( int i, int j)
	{
		int count = 0;
		for( int x = i-1; x <= i+1; x++)
		{
			for( int y = j-1; y <= j+1; y++)
			{
				if( x >= 0 && y >= 0 && x < size && y < size)
				{
					if( Minefield[x][y].hasMine() && !(x == i && y == j)) count++;
				}
			}
		}
		return count;
	}

	/**
	 * Exception used for signaling grammatical errors in Minesweeper files
	 */
	@SuppressWarnings("serial")
	public static class ParseException extends Exception {
	    public ParseException(String msg) {
	        super(msg);
	    }
	}

}
