package net.ashwork.mc.dimensions.storage.depositable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public record HasBlockState(BlockPredicate predicate) implements DepositablePredicate {

    public static final MapCodec<HasBlockState> CODEC = BlockPredicate.CODEC.fieldOf("predicate")
            .xmap(HasBlockState::new, HasBlockState::predicate);

    @Override
    public boolean test(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        return this.predicate.matches(level, pos);
    }

    @Override
    public MapCodec<? extends DepositablePredicate> codec() {
        return CODEC;
    }
}
