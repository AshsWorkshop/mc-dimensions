package net.ashwork.mc.dimensions.data.server.datapack;

import net.ashwork.mc.dimensions.storage.loot.entry.RandomTagEntry;
import net.ashwork.mc.dimensions.storage.loot.modifier.AppendEntryToLootPool;
import net.ashwork.mc.dimensions.storage.loot.modifier.LootTableModifier;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.List;
import java.util.Set;

public interface DimensionsLootTableModifiers {

    static void register(BootstrapContext<LootTableModifier> bootstrap) {
        // TODO: Properly determine what sifters should go in what tables
        bootstrap.register(key("add_sifters"), new AppendEntryToLootPool(
                Set.of(
                        BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY,
                        BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY,
                        BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY,
                        BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON
                ),
                List.of(RandomTagEntry.tagContents(DimensionItemTags.SIFTERS).build())
        ));
    }

    private static ResourceKey<LootTableModifier> key(String name) {
        return IdUtils.key(LootTableModifier.REGISTRY_KEY, name);
    }
}
