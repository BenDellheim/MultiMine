/**
 * 
 */
package minesweeper;

/**
 * <h1>Square - A given square in a Minesweeper Board</h1>
 * Each Square has a State (flagged, dug, or untouched), and a hasMine of TRUE or FALSE.<p>
 * Modifiers such as dig() are made synchronized to make them threadsafe.
 */
public final class Square {
	private State s;
	private Boolean hasMine;
	
	Square(){
		s = State.UNTOUCHED;
		hasMine = Boolean.FALSE;
	}
	
	/**
	 * Digs here.
	 * @return TRUE if a mine was dug, FALSE if not
	 * @modifies hasMine to FALSE regardless
	 */
	public synchronized Boolean dig(){
		s = State.DUG;
		if(hasMine) 
		{
			hasMine = Boolean.FALSE;
			return Boolean.TRUE;
		}
		else
		{
			return Boolean.FALSE;
		}
	}

	public State getState()
	{
		return s;
	}
	
	public Boolean hasMine()
	{
		return hasMine;
	}
	
	public synchronized void flag(){
		if( s == State.UNTOUCHED) s = State.FLAGGED;
	}
	
	public synchronized void unflag(){
		if( s == State.FLAGGED) s = State.UNTOUCHED;
	}
	
	/**
	 * Used for initializing the Board. Don't worry, we're professionals!
	 * @modifies hasMine to TRUE
	 */
	public void veryCarefullyBuryTheMine(){
		hasMine = Boolean.TRUE;
	}
}
