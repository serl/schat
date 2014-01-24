package schat;

import java.net.InetAddress;

import schat.events.EventListener;

public class Main implements EventListener<SecureConnection> {
	protected static ConsoleReader console = new ConsoleReader();

	public static void usage() {
		System.out.println("first parameter can be 'autotest' to autocreate two peers that send each other an encrypted message.");
		System.out.println("or can be a port to listen from, or an hostname:port to connect to");
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			usage();
			return;
		}
		if (args[0].equals("autotest")) {
			Peer peer_one = new Peer(54200);
			peer_one.startServer();
			Peer peer_two = new Peer(54300);
			peer_two.startServer();

			try { Thread.sleep(2000); }
			catch (InterruptedException e) { }

			try { peer_two.connectToClient(InetAddress.getLocalHost(), 54200); }
			catch (Exception e) { e.printStackTrace(); System.exit(1); }

			try { Thread.sleep(1000); }
			catch (InterruptedException e) { }

			SecureConnection two_to_one = peer_two.getCurrentConnection();
			SecureConnection one_to_two = peer_one.getCurrentConnection();

			two_to_one.send("test two_to_one");
			one_to_two.send("test one_to_two");

			try { Thread.sleep(3000); }
			catch (InterruptedException e) { }

			peer_one.abort();
			peer_two.abort();
			return;
		}

		int listenPort = -1;
		try {
			listenPort = Integer.parseInt(args[0]);
		} catch (Exception e) { }
		if (listenPort > 0) {
			Peer p = new Peer(listenPort);
			p.getConnectionEvent().addListener(new Main());
			p.startServer();
			return;
		}

		String[] parts = args[0].split(":");
		String hostname = "";
		int port = -1;
		try {
			hostname = parts[0];
			port = Integer.parseInt(parts[1]);
		} catch (Exception e) {}
		if (port > 0) {
			Peer p = new Peer();
			p.getConnectionEvent().addListener(new Main());
			try { p.connectToClient(InetAddress.getByName(hostname), port); }
			catch (Exception e) { e.printStackTrace(); System.exit(1); }
			try { Thread.sleep(1000); }
			catch (InterruptedException e) { }
		}
	}

	@Override
	public void eventHandler(SecureConnection arg) {
		console.setConnection(arg);
	}
}
