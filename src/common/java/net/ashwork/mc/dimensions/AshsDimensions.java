package net.ashwork.mc.dimensions;

import net.ashwork.mc.dimensions.event.CommonEvents;
import net.ashwork.mc.dimensions.event.ServerEvents;
import net.ashwork.mc.dimensions.network.PayloadRegistrar;
import net.ashwork.mc.dimensions.registry.DimensionRegistrars;
import net.ashwork.mc.dimensions.resources.DimensionResources;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(AshsDimensions.ID)
public class AshsDimensions {

    public static final String ID = "ashsdimensions";

    public AshsDimensions(IEventBus modBus) {
        DimensionRegistrars.registerEntries(modBus);
        CommonEvents.addListeners(modBus);
        ServerEvents.addListeners();
        DimensionResources.setup();
        PayloadRegistrar.register(modBus);
    }
}
