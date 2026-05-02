package net.ashwork.mc.dimensions.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.sounds.AmbientDesertBlockSoundsPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class SiftedSandBlock extends SiftedBlock {

    public SiftedSandBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        AmbientDesertBlockSoundsPlayer.playAmbientSandSounds(level, pos, random);
    }
}
