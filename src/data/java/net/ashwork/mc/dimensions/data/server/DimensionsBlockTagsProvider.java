package net.ashwork.mc.dimensions.data.server;

import net.ashwork.mc.dimensions.AshsDimensions;
import net.ashwork.mc.dimensions.registry.DimensionItems;
import net.ashwork.mc.dimensions.tags.DimensionBlockTags;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class DimensionsBlockTagsProvider extends BlockTagsProvider {

    public DimensionsBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, AshsDimensions.ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.tag(DimensionBlockTags.INFINIBURN_SHIFTING_DESERT);
    }
}
