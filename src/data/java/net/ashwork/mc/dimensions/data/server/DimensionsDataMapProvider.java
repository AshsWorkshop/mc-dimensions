package net.ashwork.mc.dimensions.data.server;

import net.ashwork.mc.dimensions.data.server.loot.DimensionsSiftingLoot;
import net.ashwork.mc.dimensions.registry.DimensionBlocks;
import net.ashwork.mc.dimensions.registry.DimensionDepositables;
import net.ashwork.mc.dimensions.registry.DimensionSounds;
import net.ashwork.mc.dimensions.storage.depositable.Depositable;
import net.ashwork.mc.dimensions.storage.depositable.DepositableTime;
import net.ashwork.mc.dimensions.storage.siftable.Siftable;
import net.ashwork.mc.dimensions.tags.DimensionDepositTimeTags;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DimensionsDataMapProvider extends DataMapProvider {

    public DimensionsDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        var time = provider.lookupOrThrow(DepositableTime.Entry.REGISTRY_KEY);

        this.builder(Depositable.DATA_MAP)
                .add(DimensionBlocks.SIFTED_SAND, Depositable.accepts().depositsTo(Blocks.SAND).create(), false);
        this.builder(DepositableTime.DATA_MAP)
                .add(
                        DimensionBlocks.SIFTED_SAND, DepositableTime.timeUntil(time)
                                .forThis(DimensionDepositables.EXPOSED_TO_SURFACE)
                                .forAnySide(DimensionDepositables.IS_AIR)
                                .forAnySide(DimensionDepositTimeTags.NEAR_WATER)
                        .create(), false
                );
        this.builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(DimensionItemTags.SIFTERS, new FurnaceFuel(300), false)
                .remove(ItemTags.NON_FLAMMABLE_WOOD);
        this.builder(Siftable.DATA_MAP)
                .add(Items.SAND.builtInRegistryHolder(), Siftable.siftAll(DimensionsSiftingLoot.key(Items.SAND), DimensionSounds.SIFTING_SAND), false);
    }
}
