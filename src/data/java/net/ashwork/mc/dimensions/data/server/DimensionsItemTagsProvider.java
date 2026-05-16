package net.ashwork.mc.dimensions.data.server;

import net.ashwork.mc.dimensions.AshsDimensions;
import net.ashwork.mc.dimensions.registry.DimensionItems;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class DimensionsItemTagsProvider extends ItemTagsProvider {

    public DimensionsItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, AshsDimensions.ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.tag(DimensionItemTags.SPECKS_GOLD).add(DimensionItems.GOLD_SPECK.value());
        this.tag(DimensionItemTags.SPECKS_IRON).add(DimensionItems.IRON_SPECK.value());
        this.tag(DimensionItemTags.SPECKS_COPPER).add(DimensionItems.COPPER_SPECK.value());
        this.tag(DimensionItemTags.SPECKS).addTags(DimensionItemTags.SPECKS_GOLD, DimensionItemTags.SPECKS_IRON, DimensionItemTags.SPECKS_COPPER);
        this.tag(DimensionItemTags.ORE_SHARDS_DIAMOND).add(DimensionItems.DIAMOND_SHARD.value());
        this.tag(DimensionItemTags.ORE_SHARDS).addTags(DimensionItemTags.ORE_SHARDS_DIAMOND);
        var sifters = this.tag(DimensionItemTags.SIFTERS);
        var nonFlammableWood = this.tag(ItemTags.NON_FLAMMABLE_WOOD);
        DimensionItems.SIFTERS.forEach((wood, sifter) -> {
            Item planks = registries.lookupOrThrow(Registries.ITEM).getOrThrow(ResourceKey.create(Registries.ITEM, Identifier.parse(wood.name()).withSuffix("_planks"))).value();
            sifters.add(sifter.value());
            if (wood == WoodType.WARPED || wood == WoodType.CRIMSON) {
                nonFlammableWood.add(sifter.value());
            }
        });
        this.tag(ItemTags.DURABILITY_ENCHANTABLE).addTags(DimensionItemTags.SIFTERS);
        // TODO: Figure out fortune enchantment
    }
}
