package net.ashwork.mc.dimensions.fabric;

import net.ashwork.mc.dimensions.Dimensions;
import net.fabricmc.api.ModInitializer;

public class FabricDimensions implements ModInitializer {

    @Override
    public void onInitialize() {
        Dimensions.init();
    }
}
