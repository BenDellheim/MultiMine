package minesweeper.server;
import java.net.*;
import java.io.*;

/**
 * <h1>Use: MinesweeperServer starts a new Thread for each connected user, which calls run().</h1>
 * Code modeled from an example on Jenkov.com:</br>
 * http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html<p>
 * 
 * USAGE NOTE: All server.minefield member functions use coordinates (x, y) to match user input.
 */
public class MinerRunnable implements Runnable {
	protected Socket socket = null;
	private static int players = 0;
	private Boolean killedByMine = false;
	private MinesweeperServer server; // A reference to the server, to use its get/set members

    public MinerRunnable(Socket s, MinesweeperServer ref) {
        socket = s;
        server = ref;
        players++;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Welcome to Minesweeper. " + players + " player(s) are playing including you.");
            out.println("Type 'help' for help.");

            // Main input loop
        	for (String line = in.readLine(); line != null; line = in.readLine()) {
        		String output = handleRequest(line);
        		if(output != null) {
        			out.println(output);
        		}
        		else break;
        	}

        	// Output null -> player disconnected
        	if( killedByMine) out.println("ANNIHILATED!!!");
        	synchronized(this)
        	{
        		players--;
        	}
        	out.close();
        	in.close();
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        } 
    }

	/**
	 * Handler for client input.<br />
	 * Makes requested mutations on game state if applicable, then returns an appropriate message to the user.
	 * 
	 * @param input (the command typed by the client)
	 * @return the String to be displayed to the client afterwards.<br />
	 * bye() and boom() may return null, which will exit the main loop.
	 */
	private String handleRequest(String input) {

		String regex = "(look)|(dig \\d+ \\d+)|(flag \\d+ \\d+)|(deflag \\d+ \\d+)|(help)|(bye)";
		if(!input.matches(regex)) {
			//invalid input
			return "Huh? [" + input + "] not recognized.";
		}
		String[] tokens = input.split(" ");
		if(tokens[0].equals("look")) {
			// 'look' request
			return server.board();
		} else if(tokens[0].equals("help")) {
			// 'help' request
			return server.help();
		} else if(tokens[0].equals("bye")) {
			// 'bye' request
			return null;
		} else {
			int x = Integer.parseInt(tokens[1]);
			int y = Integer.parseInt(tokens[2]);
			if( !server.Minefield.isValidIndex(x,y)) return "Index out of range."; 

			if(tokens[0].equals("dig")) {
				// 'dig x y' request
				Boolean dugUpMine = digWithRunaway(x, y);
				if( dugUpMine == true) return boom(); // Death!!

				// Otherwise, display the board after digging
				return server.board();

			} else if(tokens[0].equals("flag")) {
				// 'flag x y' request
				server.Minefield.flag(x, y);
				return server.board();
			} else if(tokens[0].equals("deflag")) {
				// 'deflag x y' request
				server.Minefield.unflag(x, y);
				return server.board();
			}
		}
		//should never get here
		return "";
	}

	/**
	 * Digs with a runaway effect. <br />
	 * While no neighbors have mines, it will automatically clear the field. <br />
	 * Since this can affect a lot of the board, I synchronized it.
	 * @return TRUE if a mine was dug up, FALSE otherwise
	 */
	private synchronized Boolean digWithRunaway( int x, int y)
	{
		// Dig the first square and stop if we hit a mine.
		if(server.Minefield.dig(x, y) == true) return true;
		
		if( server.Minefield.neighbors(x, y) == 0)
		{
			// If there are zero mines around (x,y), check the 8 surrounding squares.
			for( int i = x-1; i <= x+1; i++){
			for( int j = y-1; j <= y+1; j++){
				if( x == i && y == j) continue;

				// If the square is untouched, dig there and recurse if still zero.
				if( server.Minefield.isValidIndex(i, j) && !server.Minefield.isDug(i, j))
				{
					server.Minefield.dig(i, j);
					if( server.Minefield.neighbors(i, j) == 0) digWithRunaway( i, j);
				}
			}}
		}
		// No mines struck.
		return false;
	}
	
	private String boom()
	{
		String output = server.bye();
		if( output == null) killedByMine = true;
		return output;
	}

}
