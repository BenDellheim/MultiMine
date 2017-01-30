package minesweeper.server;

import java.net.*;
import java.io.*;
import minesweeper.Board;


public class MinesweeperServer {

	private boolean isStopped = false;
	private final static int PORT = 4444;
	private ServerSocket serverSocket;
	
    /**
     * Make a MinesweeperServer that listens for connections on port.
     * @param port port number, requires 0 <= port <= 65535.
     */
    public MinesweeperServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }
    
    /**
     * Run the server, listening for client connections and handling them.  
     * Never returns unless an exception is thrown.
     * @throws IOException if the main server socket is broken
     * (IOExceptions from individual clients do *not* terminate serve()).
     */
    public void serve() throws IOException {
        while (!isStopped()) {
            Socket socket = null;
            try {
                // block until a client connects
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace(); // but don't terminate serve()
            }
            // Start the client thread and listen for more connections
            new Thread(new MinerRunnable(socket)).start();
        }
    }
    
    private synchronized boolean isStopped() {
        return this.isStopped;
    }


    public synchronized void stop(){
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
/*TODO: Process args here (Problem 4)
 * 
 *		ARGS := DEBUG " " (SIZE | FILE)?
 *      DEBUG:= "true" | "false"
 *      SIZE := "-s " [0-9]+
 *      FILE := "-f " .+
 *      
 *      Example for an executable named "server": server true -s 25
 *      If neither SIZE nor FILE is provided, treat it as if the args were SIZE 10.
 */  	
    	
    	MinesweeperServer server = null;
        try {
            server = new MinesweeperServer(PORT);
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.stop();
    }

}