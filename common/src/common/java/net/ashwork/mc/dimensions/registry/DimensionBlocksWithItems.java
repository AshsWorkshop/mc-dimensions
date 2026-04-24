package net.ashwork.mc.dimensions.registry;

import net.ashwork.mc.dimensions.Dimensions;
import net.ashwork.mc.dimensions.block.SiftedBlock;
import net.ashwork.mc.multiloader.api.common.event.item.ModifyCreativeModeTabContents;
import net.minecraft.core.Holder;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static net.ashwork.mc.dimensions.registry.DimensionRegistrars.*;

public interface DimensionBlocksWithItems {

    Holder<SiftedBlock> SIFTED_SAND = BLOCK_ITEM.registerBlockWithBasicItem(
            "sifted_sand", props -> new SiftedBlock(Blocks.SAND, props), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SAND)
    );

    static void register() {
        Dimensions.PLATFORM.access(ModifyCreativeModeTabContents.EVENT)
                .apply(CreativeModeTabs.NATURAL_BLOCKS).on(DimensionBlocksWithItems::naturalBlocks);
    }

    static void naturalBlocks(ModifyCreativeModeTabContents.Output output) {
        output.insertAfter(Items.RED_SANDSTONE.getDefaultInstance(), SIFTED_SAND);
    }
}
