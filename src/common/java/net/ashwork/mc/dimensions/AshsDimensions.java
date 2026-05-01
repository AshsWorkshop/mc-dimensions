package net.ashwork.mc.dimensions;

import net.ashwork.mc.dimensions.registry.DimensionRegistrars;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(AshsDimensions.ID)
public class AshsDimensions {

    public static final String ID = "ashsdimensions";

    public AshsDimensions(IEventBus modBus) {
        DimensionRegistrars.registerEntries(modBus);
    }
}
