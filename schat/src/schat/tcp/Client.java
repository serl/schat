package schat.tcp;

import java.net.*;
import java.io.*;

import schat.events.EventSource;

public class Client extends Thread {
	private static final int BUFFER_SIZE = 10;

	protected Socket socket;
	protected ObjectInputStream inSock;
	protected ObjectOutputStream outSock;

	private EventSource<Object> dataEvent = new EventSource<Object>(BUFFER_SIZE);
	public EventSource<Object> getDataEvent() {
		return dataEvent;
	}

	protected boolean initiator;
	public boolean isInitiator() {
		return initiator;
	}

	public void run() {
		while (socket.isConnected() && !socket.isClosed()) {
			try {
				Object obj = inSock.readObject();
				dataEvent.fire(obj);
			}
			catch (EOFException e) {
				abort();
				System.err.println("Client socket closed: " + socket);
			}
			catch (Exception e) { }
		}
	}

	public void setSocket(Socket socket) throws IOException {
		this.socket = socket;
		this.initiator = false;
		createStreams();
	}

	public void connect(InetAddress addr, int port) throws IOException {
		try {
			socket = new Socket(addr, port);
			//socket.setSoTimeout(30000);
			System.err.println("Client socket created: " + socket);
		}
		catch (IOException e) {
			System.err.println("Error connecting to " + addr.toString() + ":" + port);
			throw e;
		}
		createStreams();
		this.initiator = true;
	}

	public void abort() {
		try {
			socket.close();
		}
		catch (IOException e) { }
	}

	private void createStreams() throws IOException {
		try {
			outSock = new ObjectOutputStream(socket.getOutputStream());
			inSock = new ObjectInputStream(socket.getInputStream());
		}
		catch (IOException e) {
			System.err.println("Unable to create streams on socket");
			throw e;
		}
		this.start();
	}

	public void send(Object obj) throws IOException {
		outSock.writeObject(obj);
	}
}
