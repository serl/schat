package schat.events;

import schat.net.TCP_Client;

public interface ConnectionAcceptor {
	public void ReceiveConnection(TCP_Client client);
}
