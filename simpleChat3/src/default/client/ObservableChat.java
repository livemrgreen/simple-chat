package client;

import java.io.IOException;
import java.util.Observable;

public class ObservableChat extends Observable {
	ChatClient obs;
	
	public void handleMessageFromClientUI(String message) throws IOException {
//		delegue le traitement du message a observableChat
	}

	public void handleMessageFromServer(Object msg) {
		// delegue le traitement du message a chatClient via notify
		this.notify(msg);
	}

	private void notify(Object msg) {
		// delegue le traitement du message aux observateurs via update
		obs.update(this, msg);
	}

}
