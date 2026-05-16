package net.ashwork.mc.dimensions.tags;

import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface DimensionBlockTags {

    TagKey<Block> INFINIBURN_SHIFTING_DESERT = mod("infiniburn/shifting_desert");

    private static TagKey<Block> mod(String name) {
        return IdUtils.tag(Registries.BLOCK, name);
    }
}
