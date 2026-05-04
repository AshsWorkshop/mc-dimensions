package net.ashwork.mc.dimensions.data;

import net.ashwork.mc.dimensions.AshsDimensions;
import net.ashwork.mc.dimensions.data.client.DimensionsLanguageProvider;
import net.ashwork.mc.dimensions.data.client.DimensionsModelProvider;
import net.ashwork.mc.dimensions.data.server.DimensionDataMapProvider;
import net.ashwork.mc.dimensions.data.server.DimensionDepositTimeTagsProvider;
import net.ashwork.mc.dimensions.data.server.DimensionsDatapackRegistries;
import net.ashwork.mc.dimensions.data.server.DimensionsItemTagsProvider;
import net.ashwork.mc.dimensions.data.server.DimensionsRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

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

        // Server providers
        pack.addProvider(DimensionsRecipeProvider.RunnerWrapper::new);
        pack.addProvider(DimensionsItemTagsProvider::new);
        pack.addProvider(DimensionDepositTimeTagsProvider::new);
        pack.addProvider(DimensionDataMapProvider::new);
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
