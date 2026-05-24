package net.ashwork.mc.dimensions.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.MapCodec;
import net.ashwork.mc.dimensions.serialization.DimensionSimpleMapCodec;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface SerializationUtils {

    static <A> Codec<List<A>> singleOrList(Codec<A> elementCodec) {
        return singleOrCollection(elementCodec, Codec::listOf, ImmutableList::of);
    }

    static <A, C extends Collection<A>> Codec<C> singleOrCollection(Codec<A> elementCodec, Function<Codec<A>, Codec<C>> collectionCodec, Function<A, C> collectionFunc) {
        return Codec.either(elementCodec, collectionCodec.apply(elementCodec)).xmap(
                either -> either.map(collectionFunc, Function.identity()),
                collection -> collection.size() == 1 ? Either.left(collection.iterator().next()) : Either.right(collection)
        );
    }

    static <K, V> MapCodec<Map<K, V>> simpleMap(Codec<K> keyCodec, Codec<V> elementCodec, Keyable keys) {
        return new DimensionSimpleMapCodec<>(keyCodec, elementCodec, keys);
    }

    static <A> Codec<Set<A>> setOf(Codec<A> elementCodec) {
        return setOf(elementCodec, 0, Integer.MAX_VALUE);
    }

    static <A> Codec<Set<A>> setOf(Codec<A> elementCodec, int minSize) {
        return setOf(elementCodec, minSize, Integer.MAX_VALUE);
    }

    static <A> Codec<Set<A>> setOf(Codec<A> elementCodec, int minSize, int maxSize) {
        return elementCodec.listOf(minSize, maxSize).xmap(ImmutableSet::copyOf, ImmutableList::copyOf);
    }

    static <A> Codec<Set<A>> singleOrSet(Codec<A> elementCodec, int minSize) {
        return singleOrSet(elementCodec, minSize, Integer.MAX_VALUE);
    }

    static <A> Codec<Set<A>> singleOrSet(Codec<A> elementCodec, int minSize, int maxSize) {
        return singleOrCollection(elementCodec, codec -> setOf(codec, minSize, maxSize), ImmutableSet::of);
    }
}
