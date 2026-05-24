package net.ashwork.mc.dimensions.extension;

import net.ashwork.mc.dimensions.util.ClassUtils;
import net.minecraft.world.level.storage.loot.LootPool;

public interface LootPoolExtension {

    default LootPool.Builder ashsdimensions$mutableCopyOf() {
        return ClassUtils.extensionDefault();
    }
}
