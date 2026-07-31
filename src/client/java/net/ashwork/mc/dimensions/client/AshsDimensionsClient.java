package net.ashwork.mc.dimensions.client;

import net.ashwork.mc.dimensions.AshsDimensions;
import net.ashwork.mc.dimensions.client.entity.EntityRenderers;
import net.ashwork.mc.dimensions.client.model.ExpandedItemModelGeneratorLoader;
import net.ashwork.mc.dimensions.client.neoext.ClientExtensions;
import net.ashwork.mc.dimensions.client.neoext.ModelExtensions;
import net.ashwork.mc.dimensions.client.network.ClientPayloadRegistrar;
import net.ashwork.mc.dimensions.resources.AdvancementRequirementsFlipper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.Collections;

@Mod(value = AshsDimensions.ID, dist = Dist.CLIENT)
public class AshsDimensionsClient {

    public AshsDimensionsClient(IEventBus modBus) {
        ClientExtensions.register(modBus);
        ClientPayloadRegistrar.register(modBus);
        EntityRenderers.register(modBus);
        ModelExtensions.setup();
        modBus.addListener(AshsDimensionsClient::registerLoaders);
        NeoForge.EVENT_BUS.addListener(AshsDimensionsClient::clearSyncedListeners);
    }

    private static void registerLoaders(ModelEvent.RegisterLoaders event) {
        event.register(ExpandedItemModelGeneratorLoader.ID, ExpandedItemModelGeneratorLoader.INSTANCE);
    }

    private static void clearSyncedListeners(ClientPlayerNetworkEvent.LoggingOut event) {
        AdvancementRequirementsFlipper.INSTANCE.setRequirements(Collections.emptyList());
    }
}
