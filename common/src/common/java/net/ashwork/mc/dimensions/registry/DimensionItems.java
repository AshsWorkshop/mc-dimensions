package net.ashwork.mc.dimensions.registry;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import static net.ashwork.mc.dimensions.registry.DimensionRegistrars.*;

public interface DimensionItems {

    Holder<Item> GOLD_SPECK = ITEM.registerBasicItem("gold_speck");

    static void register() {}
}
