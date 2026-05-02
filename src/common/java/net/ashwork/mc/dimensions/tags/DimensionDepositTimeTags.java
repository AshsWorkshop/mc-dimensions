package net.ashwork.mc.dimensions.tags;

import net.ashwork.mc.dimensions.storage.depositable.DepositableTime;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.tags.TagKey;

public interface DimensionDepositTimeTags {

    TagKey<DepositableTime.Entry> NEAR_WATER = tag("near_water");

    private static TagKey<DepositableTime.Entry> tag(String name) {
        return IdUtils.tag(DepositableTime.Entry.REGISTRY_KEY, name);
    }
}
