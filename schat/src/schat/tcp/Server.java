package schat.tcp;

import java.net.*;

import schat.events.EventSource;

class Server extends Thread {
	private final int port;
	private ServerSocket serverSocket;
	
	private EventSource<Client> connectionEvent = new EventSource<Client>();
	public EventSource<Client> getConnectionEvent() {
		return connectionEvent;
	}

	public Server(int port) {
		this.port = port;
	}

	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.setReuseAddress(true);
			System.err.println("Server socket created: " + serverSocket);
		}
		catch (Exception e) {
			System.err.println("Error creating server socket: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		try {
			while (true) {
				System.err.println("Server: waiting for clients...\n");

				Socket clientSocket;
				try {
					clientSocket = serverSocket.accept();
					System.err.println("Server: connection accepted: " + clientSocket);
				}
				catch (Exception e) {
					System.err.println("Server: error accepting connection: " + e.getMessage());
					e.printStackTrace();
					continue;
				}

				try {
					Client c = new Client();
					c.setSocket(clientSocket);
					connectionEvent.fire(c);
				}
				catch (Exception e) {
					System.err.println("Server: issues on thread: " + e.getMessage());
					e.printStackTrace();
					continue;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(2);
		}
	}
}
