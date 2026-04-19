package net.ashwork.mc.dimensions.neoforge.data;

import net.ashwork.mc.dimensions.Dimensions;
import net.ashwork.mc.dimensions.data.DimensionsData;
import net.neoforged.fml.common.Mod;

@Mod(Dimensions.ID)
public class NeoForgeDimensionsData {

    public NeoForgeDimensionsData() {
        DimensionsData.init();
    }
}
