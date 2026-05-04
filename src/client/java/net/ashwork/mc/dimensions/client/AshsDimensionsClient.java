package net.ashwork.mc.dimensions.client;

import net.ashwork.mc.dimensions.AshsDimensions;
import net.ashwork.mc.dimensions.client.model.ExpandedItemModelGeneratorLoader;
import net.ashwork.mc.dimensions.client.network.ClientPayloadRegistrar;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ModelEvent;

@Mod(AshsDimensions.ID)
public class AshsDimensionsClient {

    public AshsDimensionsClient(IEventBus modBus) {
        modBus.addListener(AshsDimensionsClient::registerLoaders);
        ClientPayloadRegistrar.register(modBus);
    }

    private static void registerLoaders(ModelEvent.RegisterLoaders event) {
        event.register(ExpandedItemModelGeneratorLoader.ID, ExpandedItemModelGeneratorLoader.INSTANCE);
    }
}
