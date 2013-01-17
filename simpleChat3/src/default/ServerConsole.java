// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.BufferedReader;
import java.io.InputStreamReader;

import common.ChatIF;

import server.EchoServer;

/**
 * This class constructs the UI for a chat client. It implements the chat
 * interface in order to activate the display() method. Warning: Some of the
 * code here is cloned in ServerConsole
 * 
 * @author Emmanuelle Ithier
 * @author Yannick Paz
 * @version 17 jan 2013
 */
public class ServerConsole implements ChatIF {
	// Class variables *************************************************

	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;

	// Instance variables **********************************************

	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	EchoServer server;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the ServerConsole UI.
	 * 
	 * @param host
	 *            The host to connect to.
	 * @param port
	 *            The port to connect on.
	 */
	public ServerConsole(String host, int port) {
		server = new EchoServer(port, this);
	}

	// Instance methods ************************************************

	/**
	 * This method waits for input from the console. Once it is received, it
	 * sends it to the client's message handler.
	 */
	public void accept() {
		try {
			BufferedReader fromConsole = new BufferedReader(
					new InputStreamReader(System.in));
			String message;

			while (true) {
				message = fromConsole.readLine();
				server.handleMessageFromServerUI(message);
			}
		} catch (Exception ex) {
			System.out.println("Unexpected error while reading from console!");
		}
	}

	/**
	 * This method overrides the method in the ChatIF interface. It displays a
	 * message onto the screen.
	 * 
	 * @param message
	 *            The string to be displayed.
	 */
	public void display(String message) {
		System.out.println("> " + message);
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the Client UI.
	 * 
	 * @param args
	 *            [0] The host to connect to.
	 */
	public static void main(String[] args) {
		String host = "";
		int port = -1; // The port number

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT;
		}
		try {
			host = args[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			host = "localhost";
		}

		// Si le port est inchange, il devient le port par defaut.
		if (port == -1) {
			port = DEFAULT_PORT;
		}

		ServerConsole serv = new ServerConsole(host, port);
		serv.accept(); // Wait for console data

	}
}
// End of ConsoleChat class
