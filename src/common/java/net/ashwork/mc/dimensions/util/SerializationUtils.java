package net.ashwork.mc.dimensions.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.MapCodec;
import net.ashwork.mc.dimensions.serialization.DimensionSimpleMapCodec;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface SerializationUtils {

    static <A> Codec<List<A>> singleOrList(Codec<A> elementCodec) {
        return Codec.either(elementCodec, elementCodec.listOf()).xmap(
                either -> either.map(List::of, Function.identity()),
                list -> list.size() == 1 ? Either.left(list.getFirst()) : Either.right(list)
        );
    }

    static <K, V> MapCodec<Map<K, V>> simpleMap(Codec<K> keyCodec, Codec<V> elementCodec, Keyable keys) {
        return new DimensionSimpleMapCodec<>(keyCodec, elementCodec, keys);
    }
}
