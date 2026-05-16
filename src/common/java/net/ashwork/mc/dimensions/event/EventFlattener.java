package net.ashwork.mc.dimensions.event;

import net.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventFlattener<T extends Event> {

    private final List<Consumer<T>> listeners;

    public EventFlattener(Consumer<T>... listeners) {
        this.listeners = new ArrayList<>();
        for (var listener : listeners) this.listeners.add(listener);
    }

    public EventFlattener<T> add(Consumer<T> listener) {
        this.listeners.add(listener);
        return this;
    }

    public void run(T event) {
        this.listeners.forEach(action -> action.accept(event));
    }
}
