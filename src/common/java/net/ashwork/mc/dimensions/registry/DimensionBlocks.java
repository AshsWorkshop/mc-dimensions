package net.ashwork.mc.dimensions.registry;

import net.ashwork.mc.dimensions.block.SiftedBlock;
import net.ashwork.mc.dimensions.block.SiftedSandBlock;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Function;
import java.util.function.Supplier;

import static net.ashwork.mc.dimensions.registry.DimensionRegistrars.*;

public interface DimensionBlocks {

    DeferredBlock<SiftedSandBlock> SIFTED_SAND = registerBlockWithSimpleItem(
            "sifted_sand", SiftedSandBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.SAND)
    );

    static void register(IEventBus modBus) {
        DimensionItems.BUILD_TABS.addListener(DimensionBlocks::naturalBlocks);
    }

    static void naturalBlocks(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != CreativeModeTabs.NATURAL_BLOCKS) return;

        event.insertAfter(Items.RED_SANDSTONE.getDefaultInstance(), SIFTED_SAND.value().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    private static <T extends Block> DeferredBlock<T> registerBlockWithSimpleItem(String name, Function<BlockBehaviour.Properties, T> factory, Supplier<BlockBehaviour.Properties> properties) {
        var block = BLOCK.registerBlock(name, factory, properties);
        ITEM.registerSimpleBlockItem(block);
        return block;
    }
}
