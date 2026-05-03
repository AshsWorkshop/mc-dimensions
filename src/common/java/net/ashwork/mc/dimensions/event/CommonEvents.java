package net.ashwork.mc.dimensions.event;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

public interface CommonEvents {
    EventFlattener<BuildCreativeModeTabContentsEvent> BUILD_TABS = new EventFlattener<>();

    static void addListeners(IEventBus modBus) {
        modBus.addListener((BuildCreativeModeTabContentsEvent event) -> BUILD_TABS.run(event));
    }
}
