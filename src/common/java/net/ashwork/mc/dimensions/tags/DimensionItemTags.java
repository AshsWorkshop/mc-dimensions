package net.ashwork.mc.dimensions.tags;

import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface DimensionItemTags {

    TagKey<Item> SPECKS = common("specks");
    TagKey<Item> SPECKS_GOLD = common("specks/gold");
    TagKey<Item> SPECKS_IRON = common("specks/iron");
    TagKey<Item> SPECKS_COPPER = common("specks/copper");
    TagKey<Item> ORE_SHARDS = common("ore_shards");
    TagKey<Item> ORE_SHARDS_DIAMOND = common("ore_shards/diamond");
    TagKey<Item> SIFTERS = mod("sifters");
    TagKey<Item> FOOD_SNAKE = mod("food/snake");

    TagKey<Item> ENCHANTABLE_FORTUNE = mod("enchantable/fortune");

    private static TagKey<Item> common(String name) {
        return ItemTags.create(Identifier.fromNamespaceAndPath("c", name));
    }

    private static TagKey<Item> mod(String name) {
        return IdUtils.tag(Registries.ITEM, name);
    }
}
