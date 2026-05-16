package net.ashwork.mc.dimensions.registry;

import net.ashwork.mc.dimensions.event.CommonEvents;
import net.ashwork.mc.dimensions.event.EventFlattener;
import net.ashwork.mc.dimensions.item.SifterItem;
import net.ashwork.mc.dimensions.item.component.WoodVariant;
import net.minecraft.core.Holder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.ashwork.mc.dimensions.registry.DimensionRegistrars.*;

public interface DimensionItems {

    DeferredItem<Item> GOLD_SPECK = ITEM.registerSimpleItem("gold_speck");
    DeferredItem<Item> IRON_SPECK = ITEM.registerSimpleItem("iron_speck");
    DeferredItem<Item> COPPER_SPECK = ITEM.registerSimpleItem("copper_speck");
    DeferredItem<Item> DIAMOND_SHARD = ITEM.registerSimpleItem("diamond_shard");
    Map<WoodType, DeferredItem<Item>> SIFTERS = WoodType.values().filter(wood -> !wood.name().contains(":"))
            .collect(Collectors.toUnmodifiableMap(
                    Function.identity(), type -> ITEM.registerItem(
                            type.name() + "_sifter", SifterItem::new, props -> props.component(
                                    DimensionDataComponents.WOOD_VARIANT, new WoodVariant(type)
                            ).durability(100)
                    )
            ));

    static void register(IEventBus modBus) {
        CommonEvents.BUILD_TABS.add(DimensionItems::ingredientsTab)
                .add(DimensionItems::toolsTab);
    }

    static void ingredientsTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != CreativeModeTabs.INGREDIENTS) return;

        insertBefore(event, Items.COPPER_NUGGET, COPPER_SPECK, IRON_SPECK, GOLD_SPECK, DIAMOND_SHARD);
    }

    static void toolsTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != CreativeModeTabs.TOOLS_AND_UTILITIES) return;

        insertBefore(event, Items.BUCKET, SIFTERS.values().toArray(Holder[]::new));
    }

    private static void insertBefore(BuildCreativeModeTabContentsEvent event, ItemLike last, Holder<? extends ItemLike>... items) {
        var lastStack = last.asItem().getDefaultInstance();
        for (var i = 0; i < items.length; i++) event.insertBefore(lastStack, items[i].value().asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
}
