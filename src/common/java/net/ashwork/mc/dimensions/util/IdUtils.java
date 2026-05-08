package net.ashwork.mc.dimensions.util;

import net.ashwork.mc.dimensions.AshsDimensions;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.Nullable;

import java.util.Locale;
import java.util.function.Function;

public interface IdUtils {

    static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(AshsDimensions.ID, name);
    }

    static String idString(String name) {
        return id(name).toString();
    }

    static String idPath(String name) {
        return AshsDimensions.ID + "/" + name;
    }

    static <T> ResourceKey<Registry<T>> registry(String name) {
        return ResourceKey.createRegistryKey(id(name));
    }

    static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registry, String name) {
        return ResourceKey.create(registry, id(name));
    }

    static <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registry, String name) {
        return TagKey.create(registry, id(name));
    }

    static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> packet(String name) {
        return new CustomPacketPayload.Type<>(id(name));
    }

    static <T extends Enum<T>> T enumExt(String name, Function<String, T> valueOf) {
        return valueOf.apply((AshsDimensions.ID + "_" + name).toUpperCase(Locale.ROOT));
    }

    static <T> ContextKey<T> contextKey(String name) {
        return new ContextKey<>(id(name));
    }

    static ResourceKey<Recipe<?>> recipe(String name) {
        return recipe(name, null);
    }

    static ResourceKey<Recipe<?>> recipe(String name, @Nullable String suffix) {
        return key(Registries.RECIPE, name + (suffix != null ? "_" + suffix : ""));
    }
}
