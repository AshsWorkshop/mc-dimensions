package net.ashwork.mc.dimensions.fabric;

import net.ashwork.mc.dimensions.data.DimensionsData;
import net.ashwork.mc.multiloader.api.data.generator.PackFactory;
import net.ashwork.mc.multiloader.fabric.data.FabricDataLoader;
import net.ashwork.mc.multiloader.fabric.data.generator.FabricPackFactory;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;

public class FabricDimensionsData implements DataGeneratorEntrypoint {

    private final FabricPackFactory packs;

    public FabricDimensionsData() {
        DimensionsData.init();
        this.packs = (FabricPackFactory) DimensionsData.PLATFORM.access(PackFactory.EXT);
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        this.packs.registerProviders(fabricDataGenerator);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        this.packs.registerRegistries(registryBuilder);
    }
}
