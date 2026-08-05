package net.ashwork.mc.dimensions.client.network;

import net.ashwork.mc.dimensions.client.resources.ClientAdvancementRequirementsFlipper;
import net.ashwork.mc.dimensions.network.ClientboundFlipAdvancementRequirementsDataPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface ClientPayloadRegistrar {

    static void register(IEventBus modBus) {
        modBus.addListener(ClientPayloadRegistrar::registerHandlers);
    }

    static void registerHandlers(RegisterClientPayloadHandlersEvent event) {
        event.register(ClientboundFlipAdvancementRequirementsDataPayload.TYPE, ClientPayloadRegistrar::handleFlipData);
    }

    static void handleFlipData(ClientboundFlipAdvancementRequirementsDataPayload payload, IPayloadContext ctx) {
        ClientAdvancementRequirementsFlipper.INSTANCE.setRequirements(payload.advancements());
    }
}
