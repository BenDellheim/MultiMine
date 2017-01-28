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

    public MinerRunnable(Socket s) {
        this.socket = s;
    }

    public void run() {
        
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

//TODO: Fix the next line's variable; it displays "2" when only I am connected.
            out.println("Welcome to Minesweeper. " + Thread.activeCount() + " people are playing including you.");
            out.println("Type 'help' for help.");

        	for (String line = in.readLine(); line != null; line = in.readLine()) {
        		String output = handleRequest(line);
        		if(output != null) {
        			out.println(output);
        		}
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
	private static String handleRequest(String input) {

		String regex = "(look)|(dig \\d+ \\d+)|(flag \\d+ \\d+)|(deflag \\d+ \\d+)|(help)|(bye)";
		if(!input.matches(regex)) {
			//invalid input
			return null;
		}
		String[] tokens = input.split(" ");
		if(tokens[0].equals("look")) {
			// 'look' request
			//TODO Question 5
		} else if(tokens[0].equals("help")) {
			// 'help' request
			//TODO Question 5
		} else if(tokens[0].equals("bye")) {
			// 'bye' request
			//TODO Question 5
		} else {
			int x = Integer.parseInt(tokens[1]);
			int y = Integer.parseInt(tokens[2]);
			if(tokens[0].equals("dig")) {
				// 'dig x y' request
				//TODO Question 5
			} else if(tokens[0].equals("flag")) {
				// 'flag x y' request
				//TODO Question 5
			} else if(tokens[0].equals("deflag")) {
				// 'deflag x y' request
				//TODO Question 5
			}
		}
		//should never get here
		return "";
	}
    

}
