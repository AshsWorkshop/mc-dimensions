package net.ashwork.mc.dimensions.data;

import net.ashwork.mc.dimensions.AshsDimensions;
import net.ashwork.mc.dimensions.data.client.DimensionsLanguageProvider;
import net.ashwork.mc.dimensions.data.client.DimensionsModelProvider;
import net.ashwork.mc.dimensions.data.client.DimensionsSoundDefinitionsProvider;
import net.ashwork.mc.dimensions.data.server.DimensionsBlockTagsProvider;
import net.ashwork.mc.dimensions.data.server.DimensionsDataMapProvider;
import net.ashwork.mc.dimensions.data.server.DimensionsDepositTimeTagsProvider;
import net.ashwork.mc.dimensions.data.server.datapack.DimensionsDatapackRegistries;
import net.ashwork.mc.dimensions.data.server.DimensionsItemTagsProvider;
import net.ashwork.mc.dimensions.data.server.DimensionsRecipeProvider;
import net.ashwork.mc.dimensions.data.server.loot.DimensionsBlockLoot;
import net.ashwork.mc.dimensions.data.server.loot.DimensionsSiftingLoot;
import net.ashwork.mc.dimensions.storage.siftable.Siftable;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

@Mod(AshsDimensions.ID)
public class AshsDimensionsData {

    public AshsDimensionsData(IEventBus modBus) {
        modBus.addListener(AshsDimensionsData::gatherData);
    }

    private static void gatherData(GatherDataEvent.Client event) {
        // Datapack registries
        event.createDatapackRegistryObjects(DimensionsDatapackRegistries.register(new RegistrySetBuilder()));

        var pack = new PackWrapper(
                event.getGenerator().getPackGenerator(true, AshsDimensions.ID, ""),
                event.getLookupProvider()
        );

        // Client providers
        pack.addProvider(DimensionsLanguageProvider::new);
        pack.addProvider(DimensionsModelProvider::new);
        pack.addProvider(DimensionsSoundDefinitionsProvider::new);

        // Server providers
        pack.addProvider(DimensionsRecipeProvider.RunnerWrapper::new);
        pack.addProvider(DimensionsBlockTagsProvider::new);
        pack.addProvider(DimensionsItemTagsProvider::new);
        pack.addProvider(DimensionsDepositTimeTagsProvider::new);
        pack.addProvider(DimensionsDataMapProvider::new);
        pack.addProvider((output, registries) -> new LootTableProvider(
                output, Collections.emptySet(), List.of(
                        new LootTableProvider.SubProviderEntry(DimensionsSiftingLoot::new, Siftable.LOOT_CONTEXT),
                        new LootTableProvider.SubProviderEntry(DimensionsBlockLoot::new, LootContextParamSets.BLOCK)
                ), registries
        ));
    }

    private static record PackWrapper(DataGenerator.PackGenerator pack, CompletableFuture<HolderLookup.Provider> registries) {

        public <T extends DataProvider> T addProvider(DataProvider.Factory<T> factory) {
            return this.pack.addProvider(factory);
        }

        public <T extends DataProvider> T addProvider(BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, T> factory) {
            return this.pack.addProvider(output -> factory.apply(output, this.registries));
        }
    }
}
