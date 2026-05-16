package net.ashwork.mc.dimensions.data.server.datapack;

import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

public interface DimensionsBiomes {

    ResourceKey<Biome> DESERT_PLAINS = key("desert_plains");

    static void register(BootstrapContext<Biome> bootstrap) {
        var placedFeatures = bootstrap.lookup(Registries.PLACED_FEATURE);
        var worldCarvers = bootstrap.lookup(Registries.CONFIGURED_CARVER);

        // TODO: Continue to flesh out
        bootstrap.register(
                DESERT_PLAINS,
                new Biome.BiomeBuilder()
                        .hasPrecipitation(false)
                        // Set this higher for player temperature system
                        .temperature(2.0f)
                        .downfall(0f)
                        .specialEffects(
                                new BiomeSpecialEffects.Builder()
                                        .waterColor(0xB78E5C)
                                        .build()
                        )
                        .mobSpawnSettings(
                                new MobSpawnSettings.Builder()
                                        .build()
                        )
                        .generationSettings(
                                new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers)
                                        .build()
                        )
                        .build()
        );
    }

    private static ResourceKey<Biome> key(String name) {
        return IdUtils.key(Registries.BIOME, name);
    }
}
