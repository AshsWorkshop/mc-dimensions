package net.ashwork.mc.dimensions.registry;

import net.ashwork.mc.dimensions.Dimensions;
import net.ashwork.mc.multiloader.api.base.extension.LoaderExtension;
import net.ashwork.mc.multiloader.api.common.registry.BlockItemRegistrar;
import net.ashwork.mc.multiloader.api.common.registry.ItemRegistrar;
import net.ashwork.mc.multiloader.api.common.registry.Registrar;
import net.ashwork.mc.multiloader.api.common.registry.RegistrarAccessor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.List;

public class DimensionRegistrars {

    private static final List<Runnable> INIT = new ArrayList<>();

    private DimensionRegistrars() {
        throw new IllegalStateException(this.getClass().getSimpleName() + " should not be instantiated");
    }

    public static final ItemRegistrar ITEM = createRegistrar(ItemRegistrar.BASIC, DimensionItems::register);
    public static final BlockItemRegistrar BLOCK_ITEM = createRegistrar(BlockItemRegistrar.BASIC, DimensionBlocksWithItems::register);

    public static void init() {
        INIT.forEach(Runnable::run);
    }

    private static <T> Registrar<T> createRegistrar(ResourceKey<? extends Registry<T>> key, Runnable initializeEntries) {
        var registry = Dimensions.PLATFORM.access(RegistrarAccessor.EXT).create(key);
        INIT.add(initializeEntries);
        return registry;
    }

    private static <R> R createRegistrar(LoaderExtension.Key<R> key, Runnable initializeEntries) {
        var registry = Dimensions.PLATFORM.access(key);
        INIT.add(initializeEntries);
        return registry;
    }
}
