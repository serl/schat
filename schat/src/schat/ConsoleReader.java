package schat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleReader extends Thread {
	protected SecureConnection connection;
	public SecureConnection getConnection() {
		return connection;
	}
	public void setConnection(SecureConnection connection) {
		this.connection = connection;

		if (!this.isAlive())
			this.start();
	}

	public void run() {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String str;
		try {
			while ( (str=stdIn.readLine()) != null) {
				connection.send(str);
			}
		} catch (IOException e) { }
		connection.close();
	}
}
