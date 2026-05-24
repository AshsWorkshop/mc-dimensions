package net.ashwork.mc.dimensions.extension;

import net.ashwork.mc.dimensions.util.ClassUtils;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public interface LootPoolBuilderExtension {

    default LootPool.Builder ashsdimensions$addAll(List<LootPoolEntryContainer> entries) {
        return ClassUtils.extensionDefault();
    }

    default LootPool.Builder ashsdimensions$whenAll(List<LootItemCondition> conditions) {
        return ClassUtils.extensionDefault();
    }

    default LootPool.Builder ashsdimensions$applyAll(List<LootItemFunction> functions) {
        return ClassUtils.extensionDefault();
    }
}
