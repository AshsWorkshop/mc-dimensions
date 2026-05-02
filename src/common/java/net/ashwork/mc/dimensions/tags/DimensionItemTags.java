package net.ashwork.mc.dimensions.tags;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface DimensionItemTags {

    TagKey<Item> SPECKS = common("specks");
    TagKey<Item> SPECKS_GOLD = common("specks/gold");
    TagKey<Item> SPECKS_IRON = common("specks/iron");
    TagKey<Item> SPECKS_COPPER = common("specks/copper");

    private static TagKey<Item> common(String name) {
        return ItemTags.create(Identifier.fromNamespaceAndPath("c", name));
    }
}
