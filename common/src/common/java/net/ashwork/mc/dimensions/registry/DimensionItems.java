package net.ashwork.mc.dimensions.registry;

import net.ashwork.mc.dimensions.Dimensions;
import net.ashwork.mc.multiloader.api.common.event.item.ModifyCreativeModeTabContents;
import net.minecraft.core.Holder;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import static net.ashwork.mc.dimensions.registry.DimensionRegistrars.*;

public interface DimensionItems {

    Holder<Item> GOLD_SPECK = ITEM.registerBasicItem("gold_speck");
    Holder<Item> IRON_SPECK = ITEM.registerBasicItem("iron_speck");
    Holder<Item> COPPER_SPECK = ITEM.registerBasicItem("copper_speck");

    static void register() {
        Dimensions.PLATFORM.access(ModifyCreativeModeTabContents.EVENT)
                .apply(CreativeModeTabs.INGREDIENTS).on(DimensionItems::ingredientsTab);
    }

    static void ingredientsTab(ModifyCreativeModeTabContents.Output output) {
        output.insertBefore(Items.COPPER_NUGGET.getDefaultInstance(), COPPER_SPECK, IRON_SPECK, GOLD_SPECK);
    }
}
