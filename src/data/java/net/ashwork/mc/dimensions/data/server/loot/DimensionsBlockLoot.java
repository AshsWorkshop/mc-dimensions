package net.ashwork.mc.dimensions.data.server.loot;

import net.ashwork.mc.dimensions.registry.DimensionBlocks;
import net.ashwork.mc.dimensions.registry.DimensionRegistrars;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.Set;

public class DimensionsBlockLoot extends BlockLootSubProvider {

    public DimensionsBlockLoot(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        this.dropSelf(DimensionBlocks.SIFTED_SAND.value());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return DimensionRegistrars.BLOCK.getEntries().stream().map(Holder::value).toList();
    }
}
