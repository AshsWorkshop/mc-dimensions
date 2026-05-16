package net.ashwork.mc.dimensions.data.server.datapack;

import com.mojang.datafixers.util.Pair;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.Collections;
import java.util.List;

public interface DimensionsLevelStems {

    ResourceKey<LevelStem> SHIFTING_DESERT = key("shifting_desert");

    static void register(BootstrapContext<LevelStem> bootstrap) {
        var dimensionTypes = bootstrap.lookup(Registries.DIMENSION_TYPE);
        var densityFunctions = bootstrap.lookup(Registries.DENSITY_FUNCTION);
        var noiseParameters = bootstrap.lookup(Registries.NOISE);
        var biomes = bootstrap.lookup(Registries.BIOME);

        bootstrap.register(
                SHIFTING_DESERT,
                new LevelStem(
                        dimensionTypes.getOrThrow(DimensionsDimensionTypes.SHIFTING_DESERT),
                        new NoiseBasedChunkGenerator(
                                // TODO: Figure out biomes and climate parameters
                                MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(List.of(
                                        Pair.of(Climate.parameters(2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f), biomes.getOrThrow(DimensionsBiomes.DESERT_PLAINS))
                                ))),
                                Holder.direct(new NoiseGeneratorSettings(
                                        // TODO: Figure out what these parameters mean
                                        new NoiseSettings(-256, 512, 2, 2),
                                        Blocks.SAND.defaultBlockState(),
                                        Blocks.WATER.defaultBlockState(),
                                        // TODO: Figure out noise settings
                                        new NoiseRouter(
                                                DensityFunctions.zero(),
                                                DensityFunctions.zero(),
                                                DensityFunctions.zero(),
                                                DensityFunctions.zero(),
                                                DensityFunctions.zero(),
                                                DensityFunctions.zero(),
                                                DensityFunctions.zero(),
                                                DensityFunctions.zero(),
                                                DensityFunctions.zero(),
                                                DensityFunctions.zero(),
                                                DensityFunctions.zero(),
                                                DensityFunctions.zero(),
                                                DensityFunctions.zero(),
                                                DensityFunctions.zero(),
                                                DensityFunctions.zero()
                                        ),
                                        // TODO: Figure out surface rules
                                        SurfaceRules.state(Blocks.SAND.defaultBlockState()),
                                        // TODO: Figure out spawn targets (may be unecessary for this dimension)
                                        Collections.emptyList(),
                                        // Sea level at 0
                                        0,
                                        // TODO: Set to false later
                                        true,
                                        // TODO: Figure out whether this should be true
                                        false,
                                        // TODO: Figure out whether this should be true
                                        false,
                                        false
                                ))
                        )
                )
        );
    }

    private static ResourceKey<LevelStem> key(String name) {
        return IdUtils.key(Registries.LEVEL_STEM, name);
    }
}
