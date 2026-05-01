package net.ashwork.mc.dimensions.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

// To revert sifting
// For any side
// - Likelihood of conversion
// - Check conditions comparing nearby block
// - Convert to block
public class SiftedBlock extends Block {

    private final Block revertTo;

    public SiftedBlock(Block revertTo, Properties properties) {
        super(properties);
        this.revertTo = revertTo;
    }

    protected int getDelayAfterPlace() {
        return 2;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        level.scheduleTick(pos, this, this.getDelayAfterPlace());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        ticks.scheduleTick(pos, this, this.getDelayAfterPlace());
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        for (var direction : Direction.values()) {
            var adjacentState = level.getBlockState(pos.relative(direction));
            if (adjacentState.liquid() || adjacentState.getValueOrElse(BlockStateProperties.WATERLOGGED, false)) {
                level.setBlockAndUpdate(pos, this.revertTo.defaultBlockState());
                return;
            }
        }
    }
}
