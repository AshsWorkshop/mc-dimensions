package net.ashwork.mc.dimensions.registry;

import net.minecraft.core.Holder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import static net.ashwork.mc.dimensions.registry.DimensionRegistrars.*;

public interface DimensionItems {

    DeferredItem<Item> GOLD_SPECK = ITEM.registerSimpleItem("gold_speck");
    DeferredItem<Item> IRON_SPECK = ITEM.registerSimpleItem("iron_speck");
    DeferredItem<Item> COPPER_SPECK = ITEM.registerSimpleItem("copper_speck");

    static void register(IEventBus modBus) {
        modBus.addListener(DimensionItems::ingredientsTab);
    }

    static void ingredientsTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != CreativeModeTabs.INGREDIENTS) return;

        insertBefore(event, Items.COPPER_NUGGET, COPPER_SPECK, IRON_SPECK, GOLD_SPECK);
    }

    private static void insertBefore(BuildCreativeModeTabContentsEvent event, ItemLike last, Holder<? extends ItemLike>... items) {
        var lastStack = last.asItem().getDefaultInstance();
        for (var i = 0; i < items.length; i++) event.insertBefore(lastStack, items[i].value().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
}
