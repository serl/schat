package schat.events;

import java.util.ArrayList;
import java.util.List;

public class EventSource<T> {
	List<EventListener<T>> listeners = new ArrayList<EventListener<T>>();

	public void addListener(EventListener<T> l) {
		listeners.add(l);
	}

	public void fire(T arg) {
		for (EventListener<T> l : listeners)
			l.EventHandler(arg);
	}
}
