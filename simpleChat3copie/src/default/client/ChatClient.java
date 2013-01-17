package client;

//This file contains material supporting section 3.7 of the textbook:
//"Object Oriented Software Engineering" and is issued under the open-source
//license found at www.lloseng.com 

import ocsf.client.*;
import common.*;

import java.io.*;
import java.util.Observable;
import java.util.Observer;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 * 
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient implements Observer {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;
	String id;
	ObservableClient obs;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 * 
	 * @param host
	 *            The server to connect to.
	 * @param port
	 *            The port number to connect on.
	 * @param clientUI
	 *            The interface type variable.
	 */

	public ChatClient(String host, int port, ChatIF clientUI)
			throws IOException {
		this.obs = new ObservableClient(host, port);
		this.obs.addObserver(this);
		this.clientUI = clientUI;
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 * 
	 * @param msg
	 *            The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		if (((String) msg).startsWith(CommandTable.logoff)) {
			try {
				this.obs.closeConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			clientUI.display(msg.toString());
		}
	}

	/**
	 * This method handles all data coming from the UI
	 * 
	 * @param message
	 *            The message from the UI.
	 * @throws IOException
	 */

	public void handleMessageFromClientUI(String message) throws IOException {
		// delegue le traitement du message a observableChat
		try {
			if (message.startsWith(CommandTable.quit) && this.obs.isConnected()) {
				this.quit();
			} else if (message.startsWith(CommandTable.quit)
					&& !this.obs.isConnected()) {
				System.exit(0);
			} else if (message.startsWith(CommandTable.logoff)
					&& this.obs.isConnected()) {
				this.obs.sendToServer(message);
				this.obs.closeConnection();
			} else if (message.startsWith(CommandTable.logoff)
					&& !this.obs.isConnected()) {
				clientUI.display("You are already disconnected");
			} else if (message.startsWith(CommandTable.login)
					&& !this.obs.isConnected()) {
				this.obs.openConnection();
				this.obs.sendToServer(message);
			} else if (message.startsWith(CommandTable.login)
					&& this.obs.isConnected()) {
				this.clientUI.display("You are already connected");
			} else if (message.startsWith(CommandTable.getPort)) {
				this.handleMessageFromClientUI(String.valueOf(this.obs
						.getPort()));
			} else if (message.startsWith(CommandTable.setPort)
					&& !this.obs.isConnected()) {
				this.obs.setPort(Integer.parseInt(message.substring(9)));
			} else if (message.startsWith(CommandTable.setPort)
					&& this.obs.isConnected()) {
				clientUI.display("You are already connected. The port can not be changed");
			} else if (message.startsWith(CommandTable.getHost)) {
				this.handleMessageFromClientUI(String.valueOf(this.obs
						.getHost()));
			} else if (message.startsWith(CommandTable.setHost)
					&& !this.obs.isConnected()) {
				this.obs.setHost(message.substring(9));
			} else if (message.startsWith(CommandTable.setHost)
					&& this.obs.isConnected()) {
				this.clientUI
						.display("You are already connected. The host can not be changed");
			} else if (this.obs.isConnected()) {
				this.obs.sendToServer(message);
			} else {
				this.clientUI
						.display("You need to enter your login to be connected to the chat. Please use the command : #login <login>");
			}

		} catch (IOException e) {
			this.handleMessageFromClientUI("Could not send message to server.  Terminating client.");
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			this.obs.sendToServer(CommandTable.logoff);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			this.obs.closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	// <-----------------------------------------------
	// <-----------------------------------------------

	public void update(Observable o, Object arg) {
		if (arg instanceof Exception) {
			this.connectionException((Exception) arg);
		} else if (((String) arg).equals(ObservableClient.CONNECTION_CLOSED)) {
			this.connectionClosed();
		} else if (((String) arg)
				.equals(ObservableClient.CONNECTION_ESTABLISHED)) {
			clientUI.display("Connection established with the server.");
		} else {
			this.handleMessageFromServer((String) arg);
		}
	}

	/**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overridden by subclasses
	 * to perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
	protected void connectionClosed() {
		System.out.println("connexion fermee, au revoir !");
	}

	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	protected void connectionException(Exception exception) {
		System.out.println("exception de connexion suivante rencontree : "
				+ exception.toString());
		// System.out.println("il est mort jim !");
		// System.out.println("le serveur n'est plus disponible, deconnexion...");
		quit();
	}

	// la connexion est etablie, le client envoie le message
	// #login <loginid>
	protected void connectionEstablished() {
		try {
			obs.sendToServer("#login " + this.id);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
// End of ChatClient class

