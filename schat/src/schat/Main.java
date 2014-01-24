package schat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Peer peer_one = new Peer(54200);
		peer_one.startServer();
		Peer peer_two = new Peer(54300);
		peer_two.startServer();

		try { Thread.sleep(2000); }
		catch (InterruptedException e) { }

		peer_two.connectToClient(InetAddress.getLocalHost(), 54200);

		try { Thread.sleep(1000); }
		catch (InterruptedException e) { }

		SecureConnection two_to_one = peer_two.getCurrentConnection();
		SecureConnection one_to_two = peer_one.getCurrentConnection();

		two_to_one.send("test two_to_one");
		one_to_two.send("test one_to_two");
		
		/*
		try { Thread.sleep(2000); }
		catch (InterruptedException e) { }
		 */

		//peer_one.abort();
		//peer_two.abort();
	}
}
