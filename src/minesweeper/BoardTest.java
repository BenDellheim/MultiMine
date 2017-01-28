package minesweeper;

import org.junit.Test;

public class BoardTest {

	@Test
	public void BoardView(){
		Board b1 = new Board(15);
		b1.showMines();
		
		System.out.println("");
		b1.showField();
	}

}
