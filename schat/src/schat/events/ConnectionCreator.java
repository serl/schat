package schat.events;

import java.util.ArrayList;
import java.util.List;

import schat.net.TCP_Client;

public class ConnectionCreator {
	List<ConnectionAcceptor> listeners = new ArrayList<ConnectionAcceptor>();
	
	public void addListener(ConnectionAcceptor toAdd) {
		listeners.add(toAdd);
    }

    public void throwConnection(TCP_Client c) {
        for (ConnectionAcceptor l : listeners)
            l.ReceiveConnection(c);
    }
}
