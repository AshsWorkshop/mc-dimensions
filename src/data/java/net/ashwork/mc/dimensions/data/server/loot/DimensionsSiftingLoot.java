package net.ashwork.mc.dimensions.data.server.loot;

import com.mojang.datafixers.kinds.Const;
import net.ashwork.mc.dimensions.registry.DimensionBlocks;
import net.ashwork.mc.dimensions.registry.DimensionItems;
import net.ashwork.mc.dimensions.storage.loot.provider.number.EnchantmentLevelOnToolProvider;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.Sum;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public record DimensionsSiftingLoot(HolderLookup.Provider registries) implements LootTableSubProvider {

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        var enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        var fortune = enchantments.getOrThrow(Enchantments.FORTUNE);

        // 0.01% pool (1-5): Odds are (1/x^8)
        // - Gold nugget
        // - Iron nugget
        // - Copper nugget
        // - Diamond shard
        output.accept(
                key(Items.SAND), LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool().setRolls(ConstantValue.exactly(1f)).add(LootItem.lootTableItem(DimensionBlocks.SIFTED_SAND))
                        )
                        .withPool(
                                LootPool.lootPool().setRolls(new BinomialDistributionGenerator(
                                                new UniformGenerator(ConstantValue.exactly(1), Sum.sum(ConstantValue.exactly(1), new EnchantmentLevelOnToolProvider(fortune))
                                        ), ConstantValue.exactly(0.2f)))
                                        .add(LootItem.lootTableItem(Items.STICK))
                                        .add(LootItem.lootTableItem(Items.GUNPOWDER))
                                        .add(LootItem.lootTableItem(Items.BRICK))
                                        .add(LootItem.lootTableItem(Items.GREEN_DYE))
                                        .add(LootItem.lootTableItem(Items.SUGAR_CANE))
                                        .add(LootItem.lootTableItem(Items.DRY_SHORT_GRASS))
                        )
                        .withPool(
                                LootPool.lootPool().setRolls(new BinomialDistributionGenerator(
                                                new UniformGenerator(ConstantValue.exactly(1), Sum.sum(ConstantValue.exactly(1), new EnchantmentLevelOnToolProvider(fortune))
                                                ), ConstantValue.exactly(0.01f)))
                                        .add(LootItem.lootTableItem(DimensionItems.GOLD_SPECK))
                                        .add(LootItem.lootTableItem(DimensionItems.IRON_SPECK))
                                        .add(LootItem.lootTableItem(DimensionItems.COPPER_SPECK))
                                        .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(5, 0.1f)))
                                        .apply(LimitCount.limitCount(IntRange.lowerBound(1)))
                                        .apply(ApplyBonusCount.addOreBonusCount(fortune))
                        )
                        .withPool(
                                LootPool.lootPool().setRolls(new BinomialDistributionGenerator(
                                                new UniformGenerator(ConstantValue.exactly(1), Sum.sum(ConstantValue.exactly(1), new EnchantmentLevelOnToolProvider(fortune))
                                                ), ConstantValue.exactly(0.0001f)))
                                        .add(LootItem.lootTableItem(Items.GOLD_NUGGET))
                                        .add(LootItem.lootTableItem(Items.IRON_NUGGET))
                                        .add(LootItem.lootTableItem(Items.COPPER_NUGGET))
                                        .add(LootItem.lootTableItem(DimensionItems.DIAMOND_SHARD))
                                        .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(5, 0.05f)))
                                        .apply(LimitCount.limitCount(IntRange.lowerBound(1)))
                                        .apply(ApplyBonusCount.addOreBonusCount(fortune))
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
