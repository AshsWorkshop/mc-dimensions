package net.ashwork.mc.dimensions.util;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventFlattener<T extends Event> {

    private final List<Consumer<T>> listeners;

    public EventFlattener() {
        this.listeners = new ArrayList<>();
    }

    public void addListener(Consumer<T> listener) {
        this.listeners.add(listener);
    }

    public void run(T event) {
        this.listeners.forEach(action -> action.accept(event));
    }
}
