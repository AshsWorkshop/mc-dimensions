package net.ashwork.mc.dimensions.resources;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public interface DimensionResources {

    static void setup() {
        NeoForge.EVENT_BUS.addListener(DimensionResources::registerReloadListeners);
        NeoForge.EVENT_BUS.addListener(DimensionResources::sendOnReload);
    }

    static void registerReloadListeners(AddServerReloadListenersEvent event) {
        event.addListener(AdvancementRequirementsFlipper.ID, AdvancementRequirementsFlipper.INSTANCE);
    }

    static void sendOnReload(OnDatapackSyncEvent event) {
        var payload = AdvancementRequirementsFlipper.INSTANCE.createPayload();
        event.getRelevantPlayers().forEach(player -> PacketDistributor.sendToPlayer(player, payload));
    }
}
