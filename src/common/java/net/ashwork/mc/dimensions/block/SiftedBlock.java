package net.ashwork.mc.dimensions.block;

import net.ashwork.mc.dimensions.storage.depositable.Depositable;
import net.ashwork.mc.dimensions.storage.depositable.DepositableTime;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jspecify.annotations.Nullable;

public class SiftedBlock extends Block {

    public static final BooleanProperty IS_DEPOSITABLE = BooleanProperty.create("is_depositable");

    public SiftedBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.defaultBlockState().setValue(IS_DEPOSITABLE, false)
        );
    }

    protected int getTickVariationRange() {
        return 20;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        // Check data
        var depositable = state.getBlock().builtInRegistryHolder().getData(Depositable.DATA_MAP);
        // Schedule initial ticking if it can be deposited
        if (depositable != null && !state.getValue(IS_DEPOSITABLE)) {
            level.scheduleTick(pos, this, level.getRandom().nextIntBetweenInclusive(DepositableTime.MINIMUM_TICKS_TO_SCHEDULE, this.getTickVariationRange()));
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        // If the block is updated, do not allow depositing
        return state.setValue(IS_DEPOSITABLE, false);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        var time = state.getBlock().builtInRegistryHolder().getData(DepositableTime.DATA_MAP);

        // If the block can take deposits or there it no time
        if (state.getValue(IS_DEPOSITABLE) || time == null) {
            // Check what the state should be deposited to
            var depositable = state.getBlock().builtInRegistryHolder().getData(Depositable.DATA_MAP);
            if (depositable != null) {
                var result = depositable.compute(state);
                if (result != state) level.setBlockAndUpdate(pos, result);
            }
        } else {
            // Otherwise, set depositable and schedule next tick
            level.setBlockAndUpdate(pos, state.setValue(IS_DEPOSITABLE, true));
            var ticks = time.findFastestDepositTime(state, level, pos, random);
            if (ticks != Integer.MAX_VALUE) level.scheduleTick(
                    pos, this, Math.max(DepositableTime.MINIMUM_TICKS_TO_SCHEDULE, ticks + random.nextInt(this.getTickVariationRange()) - (this.getTickVariationRange() / 2))
            );
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(IS_DEPOSITABLE);
    }
}
