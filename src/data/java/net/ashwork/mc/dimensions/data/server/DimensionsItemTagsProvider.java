package net.ashwork.mc.dimensions.data.server;

import net.ashwork.mc.dimensions.AshsDimensions;
import net.ashwork.mc.dimensions.registry.DimensionItems;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class DimensionsItemTagsProvider extends ItemTagsProvider {

    public DimensionsItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, AshsDimensions.ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.tag(DimensionItemTags.SPECKS_GOLD).add(DimensionItems.GOLD_SPECK.value());
        this.tag(DimensionItemTags.SPECKS_IRON).add(DimensionItems.IRON_SPECK.value());
        this.tag(DimensionItemTags.SPECKS_COPPER).add(DimensionItems.COPPER_SPECK.value());
        this.tag(DimensionItemTags.SPECKS).addTags(DimensionItemTags.SPECKS_GOLD, DimensionItemTags.SPECKS_IRON, DimensionItemTags.SPECKS_COPPER);
        var sifters = this.tag(DimensionItemTags.SIFTERS);
        DimensionItems.SIFTERS.values().forEach(sifter -> sifters.add(sifter.value()));
    }
}
