package minesweeper;

import java.util.Random;
import java.io.*;

/**
 * <h1>Board - an NxN grid of Squares</h1>
 * Shared MUTABLE datatype that is protected by synchronization.</br>
 * As true to Minesweeper, a Board can count the neighboring mines to any Square.<p>
 * 
 * USAGE NOTE: Minefield[ ][ ] uses indexes [row][column] or (y, x),
 * whereas all member functions use coordinates (x, y) to match user input.
 */
public class Board{
	private Square[][] Minefield;
	private final int size;
	private final int MIN_SIZE = 10;
	private final int MAX_SIZE = 30;
	
	// There are 3 Constructors: Board(), Board(int boardSize), and Board(String filePath).
	public Board(){
		// Creates a default board
		size = MIN_SIZE;
		Minefield = new Square[size][size];
		
		// Initialize Minefield with default values (untouched, no mine)
		for( int row=0; row < size; row++)
		{
			for( int col=0; col < size; col++)
			{
				Minefield[row][col] = new Square();
			}
		}
		
		// Add an appropriate amount of mines (I chose 25% of the field)
		int mines = (int)(size * size * .25);
		Random rand = new Random();
		for( int m=0; m < mines; m++)
		{
			int row, col;
			do
			{
				row = rand.nextInt(size);
				col = rand.nextInt(size);
			}while(Minefield[row][col].hasMine());
			Minefield[row][col].veryCarefullyBuryTheMine(); //Y'know, because they're MINES :V
		}
		
	}

	public Board( int boardSize){
		// Creates a board given a board size
		if( boardSize < MIN_SIZE || boardSize > MAX_SIZE) size = MIN_SIZE;
		else size = boardSize;
		
		Minefield = new Square[size][size];
		
		// Initialize Minefield with default values (untouched, no mine)
		for( int row=0; row < size; row++)
		{
			for( int col=0; col < size; col++)
			{
				Minefield[row][col] = new Square();
			}
		}
		
		// Add an appropriate amount of mines (I chose 25% of the field)
		int mines = (int)(size * size * .25);
		Random rand = new Random();
		for( int m=0; m < mines; m++)
		{
			int row, col;
			do
			{
				row = rand.nextInt(size);
				col = rand.nextInt(size);
			}while(Minefield[row][col].hasMine());
			Minefield[row][col].veryCarefullyBuryTheMine(); //Y'know, because they're MINES :V
		}
	}

	public Board( String filePath) throws IOException, ParseException {
		// Creates a board by reading it from a file
		int tempSize = 0;
		try( FileReader fr = new FileReader(filePath);
			 BufferedReader br = new BufferedReader(fr);)
		{
			// Prime the file-reading loop by getting the board length (ERAUQS SI DRAOB EHT)
			String line;
			line = br.readLine().replaceAll(" ", "");
        	tempSize = line.length();
        	if(tempSize < MIN_SIZE || tempSize > MAX_SIZE) throw new ParseException("Board size " + tempSize + " not supported.");

    		Minefield = new Square[tempSize][tempSize];
    		// Initialize Minefield with default values (untouched, no mine)
    		for( int row=0; row < tempSize; row++)
    		{
    			for( int col=0; col < tempSize; col++)
    			{
    				Minefield[row][col] = new Square();
    			}
    		}
    		
        	int row = 0;
        	for( int col = 0; col < tempSize; col++)
        	{
        		if(line.charAt(col) == '1') Minefield[row][col].veryCarefullyBuryTheMine();
        		else if(line.charAt(col) == '0') { /* No mine here */ }
        		else throw new ParseException("Incorrect board format!");
        	}

        	for( row = 1; row < tempSize; row++)
        	{
    			line = br.readLine();
    			if(line == null) throw new ParseException("Incorrect board format!");
    			line = line.replaceAll(" ", "");
    			if(line.length() != tempSize) throw new ParseException("Incorrect board format!");
    			
            	for( int col = 0; col < tempSize; col++)
            	{
            		if(line.charAt(col) == '1') Minefield[row][col].veryCarefullyBuryTheMine();
            		else if(line.charAt(col) != '0') throw new ParseException("Incorrect board format!");
            	}

        	}
        	if( row != tempSize) throw new ParseException("Incorrect board format!");
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
		for( int row = 0; row < size; row++)
		{
			for( int col = 0; col < size; col++)
			{
				if(Minefield[row][col].hasMine()) System.out.print("M ");
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
		for( int row = 0; row < size; row++)
		{
			for( int col = 0; col < size; col++)
			{
				int x = col;
				int y = row;
				if(Minefield[row][col].hasMine()) System.out.print("M ");
				else System.out.print(neighbors(x, y) + " ");
			}
			System.out.print("\n");
		}
		
	}

	/**
	 * Checks if there is a mine at the specified position.
	 */
	public synchronized Boolean hasMine( int x, int y)
	{
		if( !isValidIndex(x,y)) return false; 
		int row = y;
		int col = x;
		return Minefield[row][col].hasMine();
	}

	/**
	 * @return the board as the client sees it.
	 */
	public synchronized String look(){
		String output = "";
		for( int row = 0; row < size; row++)
		{
			for( int col = 0; col < size; col++)
			{
				int x = col;
				int y = row;
				if(Minefield[row][col].getState() == State.DUG) 
				{
					if(neighbors(x,y) == 0) output += "  ";
					else output += neighbors(x,y) + " ";
				}
				else if(Minefield[row][col].getState() == State.FLAGGED) output += "F ";
				else output += "- ";
			}
			output += "\r\n";
		}
		
		return output;
	}

	/**
	 * Digs at (x,y).
	 * @return TRUE if a mine was dug, FALSE otherwise.
	 */
	public synchronized Boolean dig(int x, int y){
		if( isValidIndex(x,y)) 
		{
			int row = y;
			int col = x;
			return Minefield[row][col].dig();
		}
		return false;
	}

	/**
	 * Places a flag at (x,y).
	 */
	public synchronized void flag(int x, int y){
		if( isValidIndex(x,y)) 
		{
			int row = y;
			int col = x;
			Minefield[row][col].flag();
		}
	}
	
	/**
	 * Removes a flag at (x,y).
	 */
	public synchronized void unflag(int x, int y){
		if( isValidIndex(x,y))
		{
			int row = y;
			int col = x;
			Minefield[row][col].unflag();
		}
	}
	
	/**
	 * Counts the mines surrounding a given coordinate pair.
	 * @return the number of neighboring mines (0-8)
	 */
	public int neighbors( int x, int y)
	{
		int count = 0;
		for( int row = y-1; row <= y+1; row++){
		for( int col = x-1; col <= x+1; col++){
			if( y == row && x == col) continue; // Don't count the center value
			else if( isValidIndex(row, col) && Minefield[row][col].hasMine()) count++;
		}}
		return count;
	}

	/**
	 * Confirms the index provided is inside the board.
	 */
	public Boolean isValidIndex( int x, int y)
	{
		if( x >= 0 && y >= 0 && x < size && y < size) return true;
		return false;
	}

	/**
	 * Confirms if the index provided has been dug up.
	 */
	public Boolean isDug( int x, int y)
	{
		int row = y;
		int col = x;
		if( isValidIndex(row, col))
		{
			return (Minefield[row][col].getState() == State.DUG);
		}
		return false;
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
