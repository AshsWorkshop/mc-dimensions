package net.ashwork.mc.dimensions.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import org.jspecify.annotations.Nullable;

// TODO: Implement completely (maybe can be replaced with growingplantblock)?
// Corn stalk is ~3 blocks high
// Produces 2-3 ears of corn
// Each stage goes through 1/3 of block (so three stages per block)
// One reach maximum height, ears start to become obvious, can harvest
// - R1: Corn is almost ripe (blister) (very watery)
// - R2: Corn is ripe (milk) (just right)
// - R3: Corn is past ripe (dough) (dough-like)
// - R4: Corn has finished growing (feed) (hard and starchy)
public class TallVegetationBlock extends VegetationBlock implements BonemealableBlock {

    public static final MapCodec<TallVegetationBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("vegetation_height").forGetter(b -> b.height.getPossibleValues().getLast() + 1),
            BlockBehaviour.propertiesCodec()
    ).apply(instance, TallVegetationBlock::new));
    public static final String PROPERTY_NAME = IdUtils.idString("vegetation_height");
    public static final BooleanProperty BREAK_IF_MISSING_TOP = BooleanProperty.create("break_if_missing_top");
    private final IntegerProperty height;

    public TallVegetationBlock(int height, Properties properties) {
        super(properties);
        this.height = IntegerProperty.create(PROPERTY_NAME, 0, height - 1);
        this.registerDefaultState(this.getStateDefinition().any().setValue(this.height, 0).setValue(BREAK_IF_MISSING_TOP, false));
    }

    @Override
    protected MapCodec<? extends VegetationBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(this.height);
        builder.add(BREAK_IF_MISSING_TOP);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return super.canSurvive(state, level, pos);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return super.propagatesSkylightDown(state);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return super.isPathfindable(state, type);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack destroyedWith) {
        super.playerDestroy(level, player, pos, state, blockEntity, destroyedWith);
    }

    @Override
    protected long getSeed(BlockState state, BlockPos pos) {
        return super.getSeed(state, pos);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {

    }
}
