package client;

import java.io.IOException;
import java.util.Observable;

public class ObservableChat extends Observable {
	ChatClient obs;
	AdaptableClient adapt;

	public ObservableChat(String host, int port) {
		adapt = new AdaptableClient(host, port, this);
	}

	// <-----------------------------------------------
	// <-----------------------------------------------

	public void handleMessageFromServer(Object msg) {
		// delegue le traitement du message a chatClient via notify
		this.notify(msg);
	}

	public void notify(Object msg) {
		// delegue le traitement du message aux observateurs via update
		obs.update(this, msg);
	}

	public void sendToServer(String message) {
		// Délègue l'envoie d'un message à AdaptableClient
		try {
			adapt.sendToServer(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// <-----------------------------------------------
	// <-----------------------------------------------

	// ----------------------------------------------->
	// ----------------------------------------------->

	public void closeConnection() {
		// TODO Auto-generated method stub
		try {
			adapt.closeConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		// TODO Auto-generated method stub
		return adapt.isConnected();
	}

	public void openConnection() {
		// TODO Auto-generated method stub
		try {
			adapt.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getPort() {
		// TODO Auto-generated method stub
		return adapt.getPort();
	}

	public void setPort(int port) {
		// TODO Auto-generated method stub
		adapt.setPort(port);
	}

	public String getHost() {
		// TODO Auto-generated method stub
		return adapt.getHost();
	}

	public void setHost(String substring) {
		// TODO Auto-generated method stub
		adapt.setHost(substring);
	}

	public void quit() {
		// TODO Auto-generated method stub
		adapt.quit();
	}

	// ----------------------------------------------->
	// ----------------------------------------------->

}
