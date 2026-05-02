package net.ashwork.mc.dimensions.storage.depositable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public record AtOrAboveHeightmap(Heightmap.Types heightmap) implements DepositablePredicate {

    public static final MapCodec<AtOrAboveHeightmap> CODEC = Heightmap.Types.CODEC.fieldOf("heightmap")
            .xmap(AtOrAboveHeightmap::new, AtOrAboveHeightmap::heightmap);

    @Override
    public boolean test(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        return level.getHeight(this.heightmap, pos) <= pos.getY();
    }

    @Override
    public MapCodec<? extends DepositablePredicate> codec() {
        return CODEC;
    }
}
