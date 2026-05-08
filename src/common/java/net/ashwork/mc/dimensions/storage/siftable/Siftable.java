package net.ashwork.mc.dimensions.storage.siftable;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.context.ContextKey;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public record Siftable(List<Entry> entries, Optional<ResourceKey<LootTable>> fallback) {
    private static final Codec<Siftable> RAW_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Entry.CODEC.listOf().fieldOf("with").forGetter(Siftable::entries),
            LootTable.KEY_CODEC.optionalFieldOf("fallback").forGetter(Siftable::fallback)
    ).apply(instance, Siftable::new));
    public static final Codec<Siftable> CODEC = Codec.either(
            Entry.CODEC.listOf(1, Integer.MAX_VALUE), RAW_CODEC
    ).xmap(
            either -> either.map(Siftable::new, Function.identity()),
            siftable -> siftable.fallback().isEmpty() ? Either.left(siftable.entries()) : Either.right(siftable)
    );

    public static final DataMapType<Item, Siftable> DATA_MAP = DataMapType.builder(IdUtils.id("siftable"), Registries.ITEM, CODEC)
            .synced(CODEC, false).build();

    public static final ContextKeySet LOOT_CONTEXT = new ContextKeySet.Builder()
            .required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY).required(LootContextParams.TOOL).build();

    public Siftable(List<Entry> entries) {
        this(entries, Optional.empty());
    }

    public static boolean maybeSift(LivingEntity entity) {
        var mainHand = entity.getMainHandItem();
        var offHand = entity.getOffhandItem();
        return (mainHand.is(DimensionItemTags.SIFTERS) && offHand.typeHolder().getData(DATA_MAP) != null)
                || (offHand.is(DimensionItemTags.SIFTERS) && mainHand.typeHolder().getData(DATA_MAP) != null);
    }

    public record Entry(DataComponentMatchers predicate, ResourceKey<LootTable> loot) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                DataComponentMatchers.CODEC.forGetter(Entry::predicate),
                LootTable.KEY_CODEC.fieldOf("loot_table").forGetter(Entry::loot)
        ).apply(instance, Entry::new));
    }

    public static Builder sift() {
        return new Builder();
    }

    public static Siftable siftAll(ResourceKey<LootTable> fallback) {
        return new Siftable(Collections.emptyList(), Optional.of(fallback));
    }

    public static class Builder {
        private final ImmutableList.Builder<Entry> entries;
        private Optional<ResourceKey<LootTable>> fallback;

        private Builder() {
            this.entries = ImmutableList.builder();
            this.fallback = Optional.empty();
        }

        public Builder with(ResourceKey<LootTable> loot, UnaryOperator<DataComponentMatchers.Builder> builder) {
            this.entries.add(new Entry(builder.apply(DataComponentMatchers.Builder.components()).build(), loot));
            return this;
        }

        public Builder fallback(ResourceKey<LootTable> loot) {
            this.fallback = Optional.of(loot);
            return this;
        }

        public Siftable create() {
            var list = this.entries.build();
            if (list.isEmpty() && this.fallback.isEmpty()) {
                throw new IllegalStateException("One loot table must be assigned to the siftable item.");
            }
            return new Siftable(list, this.fallback);
        }
    }
}
