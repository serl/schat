package schat.events;

import java.util.*;

public class ByteWriter {
	List<ByteReader> listeners = new ArrayList<ByteReader>();
	
	public void addListener(ByteReader toAdd) {
		listeners.add(toAdd);
    }

    public void throwBytes(String s) {
        for (ByteReader l : listeners)
            l.stringReceived(s);
    }
}
