package net.ashwork.mc.dimensions.event;

import net.ashwork.mc.dimensions.Dimensions;
import net.ashwork.mc.multiloader.api.base.extension.LoaderExtension;
import net.ashwork.mc.multiloader.api.common.event.resources.RegisterBuiltInPacks;

public interface DimensionEvents {

    static void registerListeners() {
        Dimensions.PLATFORM.access(RegisterBuiltInPacks.EVENT).add(Dimensions.PLATFORM.withId("alternative"), false);
    }
}
