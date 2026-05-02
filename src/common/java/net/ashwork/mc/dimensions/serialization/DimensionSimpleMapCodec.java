package net.ashwork.mc.dimensions.serialization;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import com.mojang.serialization.codecs.BaseMapCodec;
import com.mojang.serialization.codecs.SimpleMapCodec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @see com.mojang.serialization.codecs.SimpleMapCodec
 * @see BaseMapCodec#decode(DynamicOps, MapLike)
 */
public class DimensionSimpleMapCodec<K, V> extends MapCodec<Map<K, V>> implements BaseMapCodec<K, V> {
    private final Codec<K> keyCodec;
    private final Codec<V> elementCodec;
    private final Keyable keys;

    public DimensionSimpleMapCodec(final Codec<K> keyCodec, final Codec<V> elementCodec, final Keyable keys) {
        this.keyCodec = keyCodec;
        this.elementCodec = elementCodec;
        this.keys = keys;
    }

    @Override
    public <T> Stream<T> keys(DynamicOps<T> ops) {
        return this.keys.keys(ops);
    }

    @Override
    public Codec<K> keyCodec() {
        return this.keyCodec;
    }

    @Override
    public Codec<V> elementCodec() {
        return this.elementCodec;
    }

    @Override
    public <T> DataResult<Map<K, V>> decode(DynamicOps<T> ops, MapLike<T> input) {
        final Object2ObjectMap<K, V> read = new Object2ObjectArrayMap<>();
        final Stream.Builder<Pair<T, T>> failed = Stream.builder();
        final Set<T> keys = this.keys(ops).collect(Collectors.toSet());

        // Filter out any keys that are not known
        final DataResult<Unit> result = input.entries().filter(pair -> keys.contains(pair.getFirst())).reduce(
                DataResult.success(Unit.INSTANCE, Lifecycle.stable()),
                (r, pair) -> {
                    final DataResult<K> key = keyCodec().parse(ops, pair.getFirst());
                    final DataResult<V> value = elementCodec().parse(ops, pair.getSecond());

                    final DataResult<Pair<K, V>> entryResult = key.apply2stable(Pair::of, value);
                    final Optional<Pair<K, V>> entry = entryResult.resultOrPartial();
                    if (entry.isPresent()) {
                        final V existingValue = read.putIfAbsent(entry.get().getFirst(), entry.get().getSecond());
                        if (existingValue != null) {
                            failed.add(pair);
                            return r.apply2stable((u, p) -> u, DataResult.error(() -> "Duplicate entry for key: '" + entry.get().getFirst() + "'"));
                        }
                    }
                    if (entryResult.isError()) {
                        failed.add(pair);
                    }

                    return r.apply2stable((u, p) -> u, entryResult);
                },
                (r1, r2) -> r1.apply2stable((u1, u2) -> u1, r2)
        );

        final Map<K, V> elements = ImmutableMap.copyOf(read);
        final T errors = ops.createMap(failed.build());

        return result.map(unit -> elements).setPartial(elements).mapError(e -> e + " missed input: " + errors);
    }

    @Override
    public <T> RecordBuilder<T> encode(Map<K, V> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
        return BaseMapCodec.super.encode(input, ops, prefix);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DimensionSimpleMapCodec<?, ?> that = (DimensionSimpleMapCodec<?, ?>) o;
        return Objects.equals(this.keyCodec, that.keyCodec) && Objects.equals(this.elementCodec, that.elementCodec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.keyCodec, this.elementCodec);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + this.keyCodec + " -> " + this.elementCodec + ']';
    }
}
