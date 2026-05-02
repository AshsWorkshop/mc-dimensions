package net.ashwork.mc.dimensions.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface MergeUtils {

    static <E, T> HolderSet<T> direct(Function<E, Holder<T>> getter, E entry, E... entries) {
        return HolderSet.direct(
                Stream.concat(Stream.of(entry), Arrays.stream(entries)).map(getter).toList()
        );
    }

    static <T> List<T> merge(List<T>... lists) {
        return Arrays.stream(lists).flatMap(List::stream).toList();
    }

    static <K, V> Map<K, List<V>> mergeMapList(Map<K, List<V>>... maps) {
        Map<K, List<V>> result = new HashMap<>();
        for (var map : maps) {
            map.forEach((key, value) -> {
                var list = result.computeIfAbsent(key, k -> new ArrayList<>());
                list.addAll(value);
            });
        }

        return result;
    }

    static <T> HolderSet<T> merge(HolderSet<T>... sets) {
        return HolderSet.direct(Arrays.stream(sets).flatMap(HolderSet::stream).distinct().toList());
    }

    static <K, V> Map<K, HolderSet<V>> mergeMapHolderSet(Map<K, HolderSet<V>>... maps) {
        Map<K, Set<Holder<V>>> result = new HashMap<>();
        for (var map : maps) {
            map.forEach((key, value) -> {
                var set = result.computeIfAbsent(key, k -> new HashSet<>());
                value.stream().forEach(holder -> set.add(holder));
            });
        }

        return result.entrySet().stream().collect(Collectors.toUnmodifiableMap(
                Map.Entry::getKey, entry -> HolderSet.direct(entry.getValue().stream().toList()))
        );
    }
}
