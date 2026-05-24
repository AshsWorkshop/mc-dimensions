package net.ashwork.mc.dimensions.registry;

import net.ashwork.mc.dimensions.event.CommonEvents;
import net.ashwork.mc.dimensions.storage.loot.entry.RandomTagEntry;
import net.ashwork.mc.dimensions.storage.loot.modifier.AppendEntryToLootPool;
import net.ashwork.mc.dimensions.storage.loot.modifier.LootTableModifier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import java.util.concurrent.atomic.AtomicReference;

import static net.ashwork.mc.dimensions.registry.DimensionRegistrars.*;

public interface DimensionLootTables {

    static void register() {
        CommonEvents.DATAPACK_REGISTRY.add(DimensionLootTables::datapackRegistry);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, DimensionLootTables::loadTable);

        registerInstance(LOOT_TABLE_MODIFIER_TYPE, "append_entry_to_pool", AppendEntryToLootPool.CODEC);
        registerInstance(LOOT_POOL_ENTRY_TYPE, "random_tag_entry", RandomTagEntry.CODEC);
    }

    static void datapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(LootTableModifier.REGISTRY_KEY, LootTableModifier.DIRECT_CODEC);
    }

    static void loadTable(LootTableLoadEvent event) {
        AtomicReference<LootTable> table = new AtomicReference<>(event.getTable());

        // Apply modifiers
        event.getRegistries().lookupOrThrow(LootTableModifier.REGISTRY_KEY).listElements()
                .forEach(modifier -> table.set(modifier.value().modify(event.getKey(), table.get())));

        // Set the table
        event.setTable(table.get());
    }
}
