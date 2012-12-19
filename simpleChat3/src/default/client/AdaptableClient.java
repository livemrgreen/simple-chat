package client;

import java.io.IOException;

import ocsf.client.*;

public class AdaptableClient extends AbstractClient {
	ObservableChat obs;

	public AdaptableClient(String host, int port) {
		super(host, port);
		// TODO Auto-generated constructor stub
	}

	public void handleMessageFromClientUI(String message) throws IOException {
		// delegue le traitement du message a observableChat
		try {
			if (message.startsWith(CommandTable.quit) && this.isConnected()) {
				this.quit();
			} else if (message.startsWith(CommandTable.quit)
					&& !this.isConnected()) {
				System.exit(0);

			} else if (message.startsWith(CommandTable.logoff)
					&& this.isConnected()) {
				sendToServer(message);
				this.closeConnection();
			} else if (message.startsWith(CommandTable.logoff)
					&& !this.isConnected()) {
				obs.display("You are already disconnected");
			} else if (message.startsWith(CommandTable.login)
					&& !this.isConnected()) {
				this.openConnection();
				sendToServer(message);
			} else if (message.startsWith(CommandTable.login)
					&& this.isConnected()) {
				obs.display("You are already connected");
			} else if (message.startsWith(CommandTable.getPort)) {
				obs.display(String.valueOf(this.getPort()));
			} else if (message.startsWith(CommandTable.setPort)
					&& !this.isConnected()) {
				this.setPort(Integer.parseInt(message.substring(9)));

			} else if (message.startsWith(CommandTable.setPort)
					&& this.isConnected()) {
				obs.display("You are already connected. The port can not be changed");
			} else if (message.startsWith(CommandTable.getHost)) {
				obs.display(String.valueOf(this.getHost()));
			} else if (message.startsWith(CommandTable.setHost)
					&& !this.isConnected()) {
				this.setHost(message.substring(9));
			} else if (message.startsWith(CommandTable.setHost)
					&& this.isConnected()) {
				obs.display("You are already connected. The host can not be changed");
			} else if (this.isConnected()) {
				sendToServer(message);
			} else {
				obs.display("You need to enter your login to be connected to the chat. Please use the command : #login <login>");
			}
		} catch (IOException e) {
			obs.display("Could not send message to server.  Terminating client.");
			quit();
		}
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		// delegue le traitement du message a ObservableChat
		obs.handleMessageFromServer(msg);
	}

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
