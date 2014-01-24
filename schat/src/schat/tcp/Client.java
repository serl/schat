package schat.tcp;

import java.net.*;
import java.io.*;

import schat.events.EventSource;

public class Client extends Thread {
	private static final int BUFFER_SIZE = 10;
	
	protected Socket socket;
	protected DataInputStream inSock;
	protected DataOutputStream outSock;

	private EventSource<String> writerEvent = new EventSource<String>(BUFFER_SIZE);
	public EventSource<String> getWriterEvent() {
		return writerEvent;
	}

	public void run() {
		while (socket.isConnected() && !socket.isClosed()) {
			try {
				String in = inSock.readUTF();
				System.err.println("received: \""+in+"\" from "+socket);
				writerEvent.fire(in);
			}
			catch (EOFException e) {
				try { socket.close(); }
				catch (IOException e1) { }
			}
			catch (Exception e) { }
		}
	}

	public void setSocket(Socket socket) throws IOException {
		this.socket = socket;
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
	}

	public void abort() {
		try {
			socket.close();
		}
		catch (IOException e) { }
	}

	private void createStreams() throws IOException {
		try {
			inSock = new DataInputStream(socket.getInputStream());
			outSock = new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException e) {
			System.err.println("Unable to create streams on socket");
			throw e;
		}
		this.start();
	}

	public void send(String s) throws IOException {
		outSock.writeUTF(s);
	}
}
