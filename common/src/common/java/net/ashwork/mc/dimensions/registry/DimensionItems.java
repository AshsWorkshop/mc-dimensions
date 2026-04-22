package net.ashwork.mc.dimensions.registry;

import net.ashwork.mc.dimensions.Dimensions;
import net.ashwork.mc.multiloader.api.common.event.item.ModifyCreativeModeTabContents;
import net.minecraft.core.Holder;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import static net.ashwork.mc.dimensions.registry.DimensionRegistrars.*;

public interface DimensionItems {

    Holder<Item> GOLD_SPECK = ITEM.registerBasicItem("gold_speck");

    static void register() {
        Dimensions.PLATFORM.access(ModifyCreativeModeTabContents.EVENT)
                .apply(CreativeModeTabs.INGREDIENTS).on(DimensionItems::ingredientsTab);
    }

    static void ingredientsTab(ModifyCreativeModeTabContents.Output output) {
        output.accept(GOLD_SPECK);
    }
}
