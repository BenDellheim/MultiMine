package minesweeper.server;

import java.net.*;
import java.io.*;
import minesweeper.Board;


public class MinesweeperServer {

	private boolean isStopped = false;
	private boolean inDebugMode = true;
	public Board Minefield;
	private final static int PORT = 4444;
	private ServerSocket serverSocket;
	
    /**
     * Make a MinesweeperServer that listens for connections on port.
     * @param port port number, requires 0 <= port <= 65535.
     */
    MinesweeperServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }
    
    /**
     * Run the server, listening for client connections and handling them.  
     * Never returns unless an exception is thrown.
     * @throws IOException if the main server socket is broken
     * (IOExceptions from individual clients do *not* terminate serve()).
     */
    private void serve() throws IOException {
    	while (!isStopped()) {
            Socket socket = null;
            try {
                // block until a client connects
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace(); // but don't terminate serve()
            }
            // Start the client thread and listen for more connections
            new Thread(new MinerRunnable(socket, this)).start();
        }
    }
    
    public synchronized boolean isStopped() {
        return this.isStopped;
    }


    private synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    /**
     * Start a MinesweeperServer running on the default port.
     */
    public static void main(String[] args) {
    	MinesweeperServer server = null;
        try {
            server = new MinesweeperServer(PORT);
            server.processArgs(args);
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.stop();
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
    	 *      If neither SIZE nor FILE is provided, the default Board constructor is used.
    	 */  	

    	try {
    		// DEBUG parameter (mandatory)
    		if( args[0].compareTo("false") == 0) inDebugMode = false;
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
    
    /**
     * @return the current board for displaying.
     */
    public String board(){
    	return Minefield.look();
    }
    
    /**
     * @return a list of commands the client can use.
     */
	public String help(){
		return "The goal of Minesweeper is to clear the field without touching any mines.\r\n" +
			   "Digging will tell you the number of mines in the surrounding squares.\r\n" +
			   "For instance, if a square says 1 and there is only one square untouched by it,\r\n" +
			   "you should flag that untouched square and not dig there.\r\n\r\n" +
			   "Command list\r\n" +
			   "============\r\n" +
				"look         Displays the board.\r\n" +
				"dig x y      Digs at (x, y). The upper left corner is (0, 0).\r\n" +
				"flag x y     Puts a flag at (x, y). Use flags to mark the mines!\r\n" +
				"deflag x y   Removes a flag at (x, y).\r\n" +
				"help         Displays this message.\r\n" +
				"bye          Leaves the game.\r\n";
	}
	
	/**
	 * Exits the game.
	 * @return null iff Debug Mode is off; this lets the client leave the main loop.</br>
	 * Otherwise if Debug Mode is on, lets the client continue the game.
	 */
	public String bye(){
		if( inDebugMode) return "YOU WERE LUCKY.";
		else return null; // Kick user if were aren't debugging
	}

}