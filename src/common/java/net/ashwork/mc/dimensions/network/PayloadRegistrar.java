package net.ashwork.mc.dimensions.network;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public interface PayloadRegistrar {

    static void register(IEventBus modBus) {
        modBus.addListener(PayloadRegistrar::registerPayloads);
    }

    static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playToClient(
                        ClientboundFlipAdvancementRequirementsDataPayload.TYPE,
                        ClientboundFlipAdvancementRequirementsDataPayload.STREAM_CODEC
                );
    }
}
