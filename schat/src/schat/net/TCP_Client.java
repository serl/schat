package schat.net;

import java.net.*;
import java.io.*;

import schat.events.*;

public class TCP_Client extends Thread {
	private Socket socket;
	private DataInputStream inSock;
	private DataOutputStream outSock;

	private ByteWriter writerEvent = new ByteWriter();
	public ByteWriter getWriterEvent() {
		return writerEvent;
	}

	public void run() {
		while (socket.isConnected() && !socket.isClosed()) {
			try {
				String in = inSock.readUTF();
				System.err.println("received: \""+in+"\" from "+socket);
				writerEvent.throwBytes(in);
			}
			catch (EOFException e) {
				try { socket.close(); }
				catch (IOException e1) { }
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setSocket(Socket socket) throws IOException {
		this.socket = socket;
		createStreams();
		this.start();
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
		this.start();
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
			System.err.println("Unable to create streams on client socket");
			throw e;
		}
	}

	public void send(String s) throws IOException {
		outSock.writeUTF(s);
	}
}
