package net.ashwork.mc.dimensions.data.server.datapack;

import net.ashwork.mc.dimensions.tags.DimensionBlockTags;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.Optional;

public interface DimensionsDimensionTypes {

    ResourceKey<DimensionType> SHIFTING_DESERT = key("shifting_desert");

    static void register(BootstrapContext<DimensionType> bootstrap) {
        bootstrap.register(
                SHIFTING_DESERT,
                new DimensionType(
                        // Cycles day to night
                        false,
                        // Sees the sky
                        true,
                        false,
                        // No ender dragon
                        false,
                        // Feel like you're moving nowhere
                        0.00001f,
                        // Sprawling underground world
                        -256,
                        // Same above ground
                        512,
                        // Within 64 blocks above ground for teleportation
                        320,
                        // TODO: Figure out what blocks should be in here
                        DimensionBlockTags.INFINIBURN_SHIFTING_DESERT,
                        // A hint of ambient lighting
                        0.05f,
                        // Monsters can spawn at any light level
                        // Monsters are not limited by block light
                        new DimensionType.MonsterSettings(ConstantInt.of(15), 15),
                        // TODO: Maybe custom? Figure it out
                        DimensionType.Skybox.OVERWORLD,
                        // TODO: Maybe custom? Figure it out
                        CardinalLighting.Type.DEFAULT,
                        // TODO: Figure out attributes
                        EnvironmentAttributeMap.EMPTY,
                        // TODO: Figure out timelines
                        HolderSet.empty(),
                        // TODO: Figure out default clock
                        Optional.empty()
                )
        );
    }

    private static ResourceKey<DimensionType> key(String name) {
        return IdUtils.key(Registries.DIMENSION_TYPE, name);
    }
}
