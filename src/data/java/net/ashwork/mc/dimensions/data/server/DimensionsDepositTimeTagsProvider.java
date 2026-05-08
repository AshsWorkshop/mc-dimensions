package net.ashwork.mc.dimensions.data.server;

import net.ashwork.mc.dimensions.AshsDimensions;
import net.ashwork.mc.dimensions.registry.DimensionDepositables;
import net.ashwork.mc.dimensions.storage.depositable.DepositableTime;
import net.ashwork.mc.dimensions.tags.DimensionDepositTimeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;

import java.util.concurrent.CompletableFuture;

public class DimensionsDepositTimeTagsProvider extends KeyTagProvider<DepositableTime.Entry> {

    public DimensionsDepositTimeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, DepositableTime.Entry.REGISTRY_KEY, lookupProvider, AshsDimensions.ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.tag(DimensionDepositTimeTags.NEAR_WATER)
                .add(DimensionDepositables.IS_WATER_FLOWING,
                        DimensionDepositables.IS_WATER_SOURCE,
                        DimensionDepositables.IS_WATERLOGGED);
    }
}
