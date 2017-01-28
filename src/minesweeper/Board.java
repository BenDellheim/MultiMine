package minesweeper;

import java.util.Random;

/**
 * <h1>Board - an NxN grid of Squares</h1>
 * Shared MUTABLE datatype that is protected by synchronization.</br>
 * Only the Square subclass has modifiers, so only it gets synchronized functions.<p>
 * As true to Minesweeper, a Board can count the neighboring mines to any Square.
 */
public class Board {
	private Square[][] Minefield;
	private final int size;
	
	// Constructor
	Board( int boardSize){
		if( boardSize < 10 || boardSize > 80) size = 20;
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
		
		// Add an appropriate amount of mines (I chose 15% of the field)
		int mines = (int)(size * size * .15);
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
}

