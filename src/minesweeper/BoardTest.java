package minesweeper;

import org.junit.Test;

public class BoardTest {

	@Test
	public void BoardView() {
//		Board b1 = new Board(15);
//		b1.showMines();
		
//		System.out.println("");
//		b1.showField();
		
		try
		{
		Board b2 = new Board("samples/TestBoard1.txt");
		b2.showField();
		System.out.println("Neighbors at (4, 0): " + b2.neighbors(4, 0));
//for(int i = 0; i<=11; i++)
//		System.out.println("Is there a mine at ("+i+", "+4+")? " + b2.hasMine(i,4));

		b2 = digWithRunaway(11, 2, b2);
		System.out.println("");
		System.out.println(b2.look());
//		System.out.println("");
//		b2.showField();
		
		}catch( Exception e){}
		
	}

	/* Called after verifying (x, y) is not a mine
	 *  If (x, y) has neighbors 0
	 * For every value of (i, j) neighbor of (x, y)
	 * -If valid index and NOT DUG
	 * --Dig it woo
	 * --If THAT value has neighbors 0, call digWithRunaway on it
	 * End For
	 */
	private Board digWithRunaway( int x, int y, Board board)
	{
		// The runaway effect; if no mines, clear the surrounding tiles automatically.
		board.dig(x, y);
		if( board.neighbors(x, y) == 0)
		{
			for( int i = x-1; i <= x+1; i++){
			for( int j = y-1; j <= y+1; j++){
				if( x == i && y == j) continue;

				if( board.isValidIndex(i, j) && !board.isDug(i, j))
				{
					board.dig(i, j);
					if( board.neighbors(i, j) == 0) board = digWithRunaway( i, j, board);
				}
			}}
		}
		return board;
	}

}
