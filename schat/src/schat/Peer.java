package schat;

import java.io.IOException;
import java.net.InetAddress;

import schat.events.EventListener;
import schat.tcp.Client;
import schat.tcp.Server;

public class Peer implements EventListener<Client> {
	public static final int DEFAULT_PORT = 54321;

	protected Client currentClient;
	protected Server server;
	protected int port;

	protected SecureConnection currentConnection;
	public SecureConnection getCurrentConnection() {
		return currentConnection;
	}

	public Peer() { this(DEFAULT_PORT); }
	public Peer(int port) {
		this.port = port;
		server = new Server(port);
		server.getConnectionEvent().addListener(this);
	}

	public void startServer() {
		server.start();
	}

	public void abort() {
		server.abort();
		currentClient.abort();
	}

	public void connectToClient(InetAddress addr, int port) throws IOException {
		Client c = new Client();
		c.connect(addr, port);
		log("Client made a new connection!");
		manageConnection(c);
	}

	protected void manageConnection(Client newClient) {
		//accept or reject new connections
		if (currentClient != null && currentClient.isAlive()) {
			newClient.abort();
			log("new connection rejected!");
		}
		else {
			currentClient = newClient;
			currentConnection = new SecureConnection(newClient);
			log("new connection established!");
		}
	}

	@Override
	public void eventHandler(Client arg) {
		log("Server received a new connection!");
		manageConnection(arg);
	}

	protected void log(String s)
	{
		System.out.println("PEER#"+port+": "+s);
	}
}
