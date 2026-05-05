package net.ashwork.mc.dimensions.client.neoext;

import net.ashwork.mc.dimensions.client.neoext.item.SifterClientExtension;
import net.ashwork.mc.dimensions.registry.DimensionItems;
import net.minecraft.core.Holder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

public interface ClientExtensions {

    static void register(IEventBus modBus) {
        modBus.addListener(ClientExtensions::registerExtensions);
    }

    private static void registerExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(SifterClientExtension.INSTANCE, DimensionItems.SIFTERS.values().toArray(Holder[]::new));
    }
}
