package net.ashwork.mc.dimensions.util;

import net.ashwork.mc.dimensions.AshsDimensions;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.Nullable;

public interface IdUtils {

    static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(AshsDimensions.ID, name);
    }

    static String idString(String name) {
        return id(name).toString();
    }

    static <T> ResourceKey<T> resourceKey(ResourceKey<? extends Registry<T>> registry, String name) {
        return ResourceKey.create(registry, id(name));
    }

    static ResourceKey<Recipe<?>> recipe(String name) {
        return recipe(name, null);
    }

    static ResourceKey<Recipe<?>> recipe(String name, @Nullable String suffix) {
        return resourceKey(Registries.RECIPE, name + (suffix != null ? "_" + suffix : ""));
    }
}
