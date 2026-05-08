package net.ashwork.mc.dimensions.registry;

import net.ashwork.mc.dimensions.storage.depositable.AtOrAboveHeightmap;
import net.ashwork.mc.dimensions.storage.depositable.Depositable;
import net.ashwork.mc.dimensions.storage.depositable.DepositableTime;
import net.ashwork.mc.dimensions.storage.depositable.HasBlockState;
import net.ashwork.mc.dimensions.storage.siftable.Siftable;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import static net.ashwork.mc.dimensions.registry.DimensionRegistrars.*;

public interface DimensionDepositables {

    ResourceKey<DepositableTime.Entry> IS_AIR = key("is_air");
    ResourceKey<DepositableTime.Entry> EXPOSED_TO_SURFACE = key("exposed_to_surface");
    ResourceKey<DepositableTime.Entry> IS_WATER_SOURCE = key("is_water_source");
    ResourceKey<DepositableTime.Entry> IS_WATER_FLOWING = key("is_water_flowing");
    ResourceKey<DepositableTime.Entry> IS_WATERLOGGED = key("is_waterlogged");

    static void register(IEventBus modBus) {
        modBus.addListener(DimensionDepositables::datapackRegistry);
        modBus.addListener(DimensionDepositables::dataMap);
        modBus.addListener(DimensionDepositables::lootContext);

        DEPOSITABLE_TYPE.register("block_state", () -> HasBlockState.CODEC);
        DEPOSITABLE_TYPE.register("heightmap", () -> AtOrAboveHeightmap.CODEC);
    }

    static void datapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(DepositableTime.Entry.REGISTRY_KEY, DepositableTime.Entry.DIRECT_CODEC);
    }

    static void dataMap(RegisterDataMapTypesEvent event) {
        event.register(Depositable.DATA_MAP);
        event.register(DepositableTime.DATA_MAP);
        event.register(Siftable.DATA_MAP);
    }

    static void lootContext(FMLConstructModEvent event) {
        event.enqueueWork(() -> LootContextParamSets.REGISTRY.put(IdUtils.id("sifting"), Siftable.LOOT_CONTEXT));
    }

    private static ResourceKey<DepositableTime.Entry> key(String name) {
        return IdUtils.key(DepositableTime.Entry.REGISTRY_KEY, name);
    }
}
