package minesweeper.server;
import java.net.*;
import java.io.*;

/**
 * <h1>Use: MinesweeperServer starts a new Thread for each connected user, which calls run().</h1>
 * Code modeled from an example on Jenkov.com:</br>
 * http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html
 */
public class MinerRunnable implements Runnable {
	protected Socket socket = null;
	private volatile int players = 0;
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

            out.println("Welcome to Minesweeper. " + players + " players are playing including you.");
            out.println("Type 'help' for help.");

        	for (String line = in.readLine(); line != null; line = in.readLine()) {
        		String output = handleRequest(line);
        		if(output != null) {
        			out.println(output);
        		}
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
	 * Handler for client input
	 * 
	 * make requested mutations on game state if applicable, then return appropriate message to the user
	 * 
	 * @param input
	 * @return
	 */
	private String handleRequest(String input) {

		String regex = "(look)|(dig \\d+ \\d+)|(flag \\d+ \\d+)|(deflag \\d+ \\d+)|(help)|(bye)";
		if(!input.matches(regex)) {
			//invalid input
			return null;
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
			return server.bye();
		} else {
			int x = Integer.parseInt(tokens[1]);
			int y = Integer.parseInt(tokens[2]);
			if(tokens[0].equals("dig")) {
				// 'dig x y' request
				Boolean result = server.Minefield.dig(x, y);
				if( result == true)
				{
					// Death! (You hit a mine)
					return boom();
				}
				else return server.board();
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
    
	private String boom()
	{
		String output = server.bye();
		if( output == null) killedByMine = true;
		return output;
	}

}
