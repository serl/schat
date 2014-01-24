package schat.events;

import java.util.ArrayList;
import java.util.List;

public class EventSource<T> {
	protected final List<EventListener<T>> listeners = new ArrayList<EventListener<T>>();
	protected final Object[] buffer;
	protected int eventIndex = 0;

	public EventSource() { this(0); }
	public EventSource(int bufferSize) {
		this.buffer = new Object[bufferSize];
	}

	@SuppressWarnings("unchecked")
	public void addListener(EventListener<T> l) {
		listeners.add(l);
		if (buffer.length > 0) {
			for (int i = 1; i <= buffer.length; i++) {
				int index = (eventIndex + i) % buffer.length;
				if (buffer[index] != null)
					l.eventHandler((T)(buffer[index]));
			}
		}
	}

	public void fire(T arg) {
		if (buffer.length > 0) {
			eventIndex = (eventIndex + 1) % buffer.length;
			buffer[eventIndex] = arg;
		}
		for (EventListener<T> l : listeners)
			l.eventHandler(arg);
	}
}
