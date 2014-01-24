package schat;

import java.io.IOException;

import schat.events.EventListener;
import schat.tcp.Client;

public class SecureConnection implements EventListener<String> {
	protected final Client client;
	
	public SecureConnection(Client c) {
		client = c;
		client.getWriterEvent().addListener(this);
	}

	@Override
	public void eventHandler(String arg) {
		System.out.println("Plaintext received: "+arg);
	}
	
	public void send (String s) throws IOException {
		client.send(s);
	}

}
