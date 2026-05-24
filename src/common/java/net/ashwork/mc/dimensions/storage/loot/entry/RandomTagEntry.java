package net.ashwork.mc.dimensions.storage.loot.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class RandomTagEntry extends LootPoolSingletonContainer {
    public static final MapCodec<RandomTagEntry> CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    TagKey.codec(Registries.ITEM).fieldOf("name").forGetter(e -> e.tag))
                    .and(singletonFields(i))
                    .apply(i, RandomTagEntry::new)
    );
    private final TagKey<Item> tag;

    public RandomTagEntry(TagKey<Item> tag, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);
        this.tag = tag;
    }

    @Override
    public MapCodec<RandomTagEntry> codec() {
        return CODEC;
    }

    @Override
    public void createItemStack(Consumer<ItemStack> output, LootContext context) {
        BuiltInRegistries.ITEM.get(this.tag).flatMap(holderSet -> holderSet.getRandomElement(context.getRandom()))
                .ifPresent(item -> output.accept(new ItemStack(item)));
    }

    public static LootPoolSingletonContainer.Builder<?> tagContents(TagKey<Item> tag) {
        return simpleBuilder((weight, quality, conditions, functions) -> new RandomTagEntry(tag, weight, quality, conditions, functions));
    }
}
