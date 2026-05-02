package net.ashwork.mc.dimensions.storage.depositable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.ashwork.mc.dimensions.util.MergeUtils;
import net.ashwork.mc.dimensions.util.SerializationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public record DepositableTime(List<HolderSet<Entry>> current, List<HolderSet<Entry>> anySide, Map<Direction, List<HolderSet<Entry>>> sides) {
    private static final Codec<DepositableTime> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SerializationUtils.singleOrList(Entry.LIST_CODEC).optionalFieldOf("this", Collections.emptyList()).forGetter(DepositableTime::current),
            SerializationUtils.singleOrList(Entry.LIST_CODEC).optionalFieldOf("any_side", Collections.emptyList()).forGetter(DepositableTime::anySide),
            SerializationUtils.simpleMap(Direction.CODEC, SerializationUtils.singleOrList(Entry.LIST_CODEC), StringRepresentable.keys(Direction.values())).forGetter(DepositableTime::sides)
    ).apply(instance, DepositableTime::new));

    public static final DataMapType<Block, DepositableTime> DATA_MAP = AdvancedDataMapType.builder(IdUtils.id("depositable_time"), Registries.BLOCK, CODEC)
            .merger((registry, first, firstValue, second, secondValue) ->
                new DepositableTime(
                        MergeUtils.merge(firstValue.current, secondValue.current),
                        MergeUtils.merge(firstValue.anySide, secondValue.anySide),
                        MergeUtils.mergeMapList(firstValue.sides, secondValue.sides)
                )
            ).build();

    public static final int MINIMUM_TICKS_TO_SCHEDULE = 2;

    public int findFastestDepositTime(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Max value if no time
        int ticksToDeposit = Integer.MAX_VALUE;

        // Check this block pos first
        ticksToDeposit = reduce(ticksToDeposit, this.current.stream(), state, level, pos, random);
        // Then check the surrounding directions
        for (var direction : Direction.values()) {
            var adjacentPos = pos.relative(direction);
            var adjacentState = level.getBlockState(adjacentPos);
            ticksToDeposit = reduce(ticksToDeposit, Stream.concat(
                    this.anySide.stream(), sides.getOrDefault(direction, Collections.emptyList()).stream()
            ), adjacentState, level, adjacentPos, random);
        }

        return Math.max(MINIMUM_TICKS_TO_SCHEDULE, ticksToDeposit);
    }

    private static int reduce(int identity, Stream<HolderSet<Entry>> list, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        return list.flatMap(HolderSet::stream).filter(holder -> holder.value().predicate.test(state, level, pos, random))
                .mapToInt(holder -> holder.value().ticks).reduce(identity, (a, b) -> Math.min(a, b));
    }

    public record Entry(DepositablePredicate predicate, int ticks) {
        public static final ResourceKey<Registry<Entry>> REGISTRY_KEY = IdUtils.registry("deposit_time");

        public static final Codec<Entry> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                DepositablePredicate.TYPE_REGISTRY.byNameCodec().dispatchMap(DepositablePredicate::codec, Function.identity()).forGetter(Entry::predicate),
                Codec.INT.fieldOf("ticks_to_deposit").forGetter(Entry::ticks)
        ).apply(instance, Entry::new));
        public static final Codec<HolderSet<Entry>> LIST_CODEC = RegistryCodecs.homogeneousList(REGISTRY_KEY, DIRECT_CODEC);
    }

    public static Builder timeUntil(HolderGetter<DepositableTime.Entry> getter) {
        return new Builder(getter);
    }

    public static class Builder {
        private final HolderGetter<DepositableTime.Entry> getter;
        private final List<HolderSet<Entry>> current;
        private final List<HolderSet<Entry>> anySide;
        private final Map<Direction, List<HolderSet<Entry>>> sides;

        private Builder(HolderGetter<DepositableTime.Entry> getter) {
            this.getter = getter;
            this.current = new ArrayList<>();
            this.anySide = new ArrayList<>();
            this.sides = new EnumMap<>(Direction.class);
        }

        public Builder forThis(ResourceKey<Entry> entry, ResourceKey<Entry>... entries) {
            return this.forThis(MergeUtils.direct(this.getter::getOrThrow, entry, entries));
        }

        public Builder forThis(TagKey<Entry> tag) {
            return this.forThis(this.getter.getOrThrow(tag));
        }

        private Builder forThis(HolderSet<Entry> current) {
            this.current.add(current);
            return this;
        }

        public Builder forAnySide(ResourceKey<Entry> entry, ResourceKey<Entry>... entries) {
            return this.forAnySide(MergeUtils.direct(this.getter::getOrThrow, entry, entries));
        }

        public Builder forAnySide(TagKey<Entry> tag) {
            return this.forAnySide(this.getter.getOrThrow(tag));
        }

        private Builder forAnySide(HolderSet<Entry> anySide) {
            this.anySide.add(anySide);
            return this;
        }

        public Builder forSide(Direction side, ResourceKey<Entry> entry, ResourceKey<Entry>... entries) {
            return this.forSide(side, MergeUtils.direct(this.getter::getOrThrow, entry, entries));
        }

        public Builder forSide(Direction side, TagKey<Entry> tag) {
            return this.forSide(side, this.getter.getOrThrow(tag));
        }

        private Builder forSide(Direction side, HolderSet<Entry> set) {
            this.sides.computeIfAbsent(side, s -> new ArrayList<>()).add(set);
            return this;
        }

        public DepositableTime create() {
            if (this.current.size() + this.anySide.size() + this.sides.size() == 0) {
                throw new IllegalStateException("There must be at least one entry indicating how many ticks until deposit.");
            }
            return new DepositableTime(this.current, this.anySide, this.sides);
        }
    }
}
