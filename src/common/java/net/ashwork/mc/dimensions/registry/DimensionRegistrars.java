package net.ashwork.mc.dimensions.registry;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.dimensions.AshsDimensions;
import net.ashwork.mc.dimensions.storage.depositable.DepositablePredicate;
import net.ashwork.mc.dimensions.storage.matcher.DimensionValueMatcher;
import net.ashwork.mc.dimensions.util.ClassUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DimensionRegistrars {
    private static final List<RegistrarEntry> INIT = new ArrayList<>();

    public static final DeferredRegister<SoundEvent> SOUND_EVENT = createRegistrar(Registries.SOUND_EVENT, DimensionSounds::register);
    public static final DeferredRegister.DataComponents DATA_COMPONENT = createRegistrar(
            modId -> DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, modId), DimensionDataComponents::register
    );
    public static final DeferredRegister.Items ITEM = createRegistrar(DeferredRegister::createItems, DimensionItems::register);
    public static final DeferredRegister.Blocks BLOCK = createRegistrar(DeferredRegister::createBlocks, DimensionBlocks::register);
    public static final DeferredRegister<MapCodec<? extends DepositablePredicate>> DEPOSITABLE_TYPE = createRegistrar(DepositablePredicate.TYPE_KEY, DimensionDepositables::register);
    public static final DeferredRegister<MapCodec<? extends DimensionValueMatcher>> VALUE_MATCHER = createRegistrar(DimensionValueMatcher.TYPE_KEY, DimensionValueMatchers::register);
    public static final DeferredRegister<MapCodec<? extends NumberProvider>> NUMBER_PROVIDER = createRegistrar(Registries.LOOT_NUMBER_PROVIDER_TYPE, DimensionNumberProviders::register);
    public static final DeferredRegister.Entities ENTITY = createRegistrar(DeferredRegister::createEntities, DimensionEntities::register);

    static <T> void registerInstance(DeferredRegister<T> registrar, String name, T instance) {
        registrar.register(name, () -> instance);
    }

    private DimensionRegistrars() {
        ClassUtils.doNotInstantiate(this);
    }

    public static void registerEntries(IEventBus modBus) {
        modBus.addListener(DimensionRegistrars::newRegistry);

        INIT.forEach(entry -> {
            // Register to the mod bus
            entry.registrar().register(modBus);
            // Initialize entries
            entry.initializeEntries().accept(modBus);
        });
    }

    private static void newRegistry(NewRegistryEvent event) {
        event.register(DepositablePredicate.TYPE_REGISTRY);
        event.register(DimensionValueMatcher.TYPE_REGISTRY);
    }

    private static <T> DeferredRegister<T> createRegistrar(ResourceKey<? extends Registry<T>> key, Runnable initializeEntries) {
        return createRegistrar(modId -> DeferredRegister.create(key, modId), initializeEntries);
    }

    private static <T> DeferredRegister<T> createRegistrar(ResourceKey<? extends Registry<T>> key, Consumer<IEventBus> initializeEntries) {
        return createRegistrar(modId -> DeferredRegister.create(key, modId), initializeEntries);
    }

    private static <T, R extends DeferredRegister<T>> R createRegistrar(Function<String, R> factory, Runnable initializeEntries) {
        return createRegistrar(factory, bus -> initializeEntries.run());
    }

    private static <T, R extends DeferredRegister<T>> R createRegistrar(Function<String, R> factory, Consumer<IEventBus> initializeEntries) {
        var registrar = factory.apply(AshsDimensions.ID);
        INIT.add(new RegistrarEntry(registrar, initializeEntries));
        return registrar;
    }

    private static record RegistrarEntry(DeferredRegister<?> registrar, Consumer<IEventBus> initializeEntries) {}
}
