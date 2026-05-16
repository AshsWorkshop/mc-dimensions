package net.ashwork.mc.dimensions.event;

import net.ashwork.mc.dimensions.registry.DimensionEnchantments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public interface CommonEvents {
    EventFlattener<BuildCreativeModeTabContentsEvent> BUILD_TABS = new EventFlattener<>();
    EventFlattener<DataPackRegistryEvent.NewRegistry> DATAPACK_REGISTRY = new EventFlattener<>(
            DimensionEnchantments::datapackRegistry
    );

    static void addListeners(IEventBus modBus) {
        modBus.addListener((BuildCreativeModeTabContentsEvent event) -> BUILD_TABS.run(event));
        modBus.addListener((DataPackRegistryEvent.NewRegistry event) -> DATAPACK_REGISTRY.run(event));
    }
}
