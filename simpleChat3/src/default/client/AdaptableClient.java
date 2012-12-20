package client;

import java.io.IOException;

import ocsf.client.*;

public class AdaptableClient extends AbstractClient {
	ObservableChat obs;

	public AdaptableClient(String host, int port) {
		super(host, port);
		// TODO Auto-generated constructor stub
	}

//	<-----------------------------------------------
//	<-----------------------------------------------
	
	protected void handleMessageFromServer(Object msg) {
		// delegue le traitement du message a ObservableChat
		obs.handleMessageFromServer(msg);
	}
	
//	<-----------------------------------------------
//	<-----------------------------------------------

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
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
		System.out.println("il est mort jim !");
		System.out.println("le serveur n'est plus disponible, deconnexion...");
		quit();
	}

}
