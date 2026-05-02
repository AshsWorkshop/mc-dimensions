package net.ashwork.mc.dimensions.data.server;

import net.ashwork.mc.dimensions.registry.DimensionDepositables;
import net.ashwork.mc.dimensions.storage.depositable.AtOrAboveHeightmap;
import net.ashwork.mc.dimensions.storage.depositable.DepositableTime;
import net.ashwork.mc.dimensions.storage.depositable.HasBlockState;
import net.ashwork.mc.dimensions.storage.matcher.NotMatcher;
import net.minecraft.advancements.criterion.BlockPredicate;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;

public interface DimensionsDatapackRegistries {

    static RegistrySetBuilder register(RegistrySetBuilder builder) {
        return builder.add(DepositableTime.Entry.REGISTRY_KEY, DimensionsDatapackRegistries::depositTime);
    }

    static void depositTime(BootstrapContext<DepositableTime.Entry> bootstrap) {
        var blocks = bootstrap.lookup(Registries.BLOCK);

        bootstrap.register(DimensionDepositables.IS_AIR, new DepositableTime.Entry(
                new HasBlockState(BlockPredicate.Builder.block().of(blocks, BlockTags.AIR).build()),
                overworldDays(15f, 1f)
        ));
        bootstrap.register(DimensionDepositables.EXPOSED_TO_SURFACE, new DepositableTime.Entry(
                new AtOrAboveHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
                overworldDays(10f, 1f)
        ));
        bootstrap.register(DimensionDepositables.IS_WATER_SOURCE, new DepositableTime.Entry(
                new HasBlockState(BlockPredicate.Builder.block().of(blocks, Blocks.WATER).setProperties(
                        StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.LEVEL, 0)
                ).build()),
                overworldDays(5f)
        ));
        bootstrap.register(DimensionDepositables.IS_WATER_FLOWING, new DepositableTime.Entry(
                new HasBlockState(BlockPredicate.Builder.block().of(blocks, Blocks.WATER).setProperties(
                        StatePropertiesPredicate.Builder.properties().with(BlockStateProperties.LEVEL, new NotMatcher(
                                new StatePropertiesPredicate.ExactMatcher(Integer.toString(0))
                        ))
                ).build()),
                overworldDays(3f, .5f)
        ));
        bootstrap.register(DimensionDepositables.IS_WATERLOGGED, new DepositableTime.Entry(
                new HasBlockState(BlockPredicate.Builder.block().setProperties(
                        StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.WATERLOGGED, true)
                ).build()),
                overworldDays(7f)
        ));
    }

    private static int overworldDays(float days) {
        // Each overworld day is 24,000 ticks
        return Mth.floor(days * 24_000);
    }

    private static IntProvider overworldDays(float days, float range) {
        var ticks = overworldDays(days);
        var ticksRange = overworldDays(range);
        return UniformInt.of(ticks - ticksRange, ticks + ticksRange);
    }
}
