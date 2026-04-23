package net.ashwork.mc.dimensions;

import net.ashwork.mc.dimensions.event.DimensionEvents;
import net.ashwork.mc.dimensions.registry.DimensionRegistrars;
import net.ashwork.mc.multiloader.api.base.ModLoader;
import net.ashwork.mc.multiloader.api.common.CommonModLoaderAccessor;

public interface Dimensions {

    String ID = "ashsdimensions";
    ModLoader PLATFORM = CommonModLoaderAccessor.INSTANCE.create(ID);

    static void init() {
        DimensionRegistrars.init();
        DimensionEvents.registerListeners();
    }
}
