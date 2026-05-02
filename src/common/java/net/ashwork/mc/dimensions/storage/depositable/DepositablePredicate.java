package net.ashwork.mc.dimensions.storage.depositable;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.RegistryBuilder;

public interface DepositablePredicate {

    ResourceKey<Registry<MapCodec<? extends DepositablePredicate>>> TYPE_KEY = IdUtils.registry("depositable_predicate_type");
    Registry<MapCodec<? extends DepositablePredicate>> TYPE_REGISTRY = new RegistryBuilder<>(TYPE_KEY).create();

    boolean test(BlockState state, ServerLevel level, BlockPos pos, RandomSource random);

    MapCodec<? extends DepositablePredicate> codec();
}
