package schat.tcp;

import java.net.*;

import schat.events.EventSource;

public class Server extends Thread {
	private static final int BUFFER_SIZE = 10;

	private final int port;
	private ServerSocket serverSocket;
	private boolean aborted = false;
	
	private EventSource<Client> connectionEvent = new EventSource<Client>(BUFFER_SIZE);
	public EventSource<Client> getConnectionEvent() {
		return connectionEvent;
	}

	public Server(int port) {
		this.port = port;
	}
	
	public void abort() {
		aborted = true;
	}

	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			serverSocket.setReuseAddress(true);
			serverSocket.setSoTimeout(5000);
			System.err.println("Server socket created: " + serverSocket);
		}
		catch (Exception e) {
			System.err.println("Error creating server socket: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		try {
			while (!aborted) {
				///System.err.println("Server: waiting for clients...");

				Socket clientSocket;
				try {
					clientSocket = serverSocket.accept();
					System.err.println("Server: connection accepted: " + clientSocket);
				}
				catch (SocketTimeoutException e) {
					continue;
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
