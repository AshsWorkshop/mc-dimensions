package net.ashwork.mc.dimensions.data.server.loot;

import net.ashwork.mc.dimensions.registry.DimensionBlocks;
import net.ashwork.mc.dimensions.registry.DimensionItems;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public record DimensionsSiftingLoot(HolderLookup.Provider registries) implements LootTableSubProvider {

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        // TODO: Figure out what items sand can be sifted to
        output.accept(
                key(Items.SAND), LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool().setRolls(ConstantValue.exactly(1f)).add(LootItem.lootTableItem(DimensionBlocks.SIFTED_SAND))
                        )
                        .withPool(
                                LootPool.lootPool().setRolls(ConstantValue.exactly(1f)).add(LootItem.lootTableItem(DimensionItems.GOLD_SPECK))
                        )
        );
    }

    public static ResourceKey<LootTable> key(Item item) {
        return ResourceKey.create(
                Registries.LOOT_TABLE,
                item.builtInRegistryHolder().unwrapKey().get().identifier().withPrefix(IdUtils.idPath("sifting") + "/")
        );
    }
}
