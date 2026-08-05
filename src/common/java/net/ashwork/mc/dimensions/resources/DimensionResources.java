package net.ashwork.mc.dimensions.resources;

import net.ashwork.mc.dimensions.resources.advancement.AdvancementRequirementsFlipperListener;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Collections;

public interface DimensionResources {

    static void setup() {
        NeoForge.EVENT_BUS.addListener(DimensionResources::registerReloadListeners);
        NeoForge.EVENT_BUS.addListener(DimensionResources::sendOnReload);
    }

    static void registerReloadListeners(AddServerReloadListenersEvent event) {
        event.addRetainedListener(AdvancementRequirementsFlipperListener.ID, new AdvancementRequirementsFlipperListener());
    }

    static void sendOnReload(OnDatapackSyncEvent event) {
        var payload = event.getPlayerList().getServer().getServerResources().managers().getListener(AdvancementRequirementsFlipperListener.ID).createPayload();
        event.getRelevantPlayers().forEach(player -> PacketDistributor.sendToPlayer(player, payload));
    }
}
