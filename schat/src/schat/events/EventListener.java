package schat.events;

public interface EventListener<T> {
	void eventHandler(T arg);
}
