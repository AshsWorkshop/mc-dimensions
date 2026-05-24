package net.ashwork.mc.dimensions.storage.loot.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.dimensions.util.SerializationUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import java.util.List;
import java.util.Set;

public class AppendEntryToLootPool extends AbstractLootTableModifier {

    public static final MapCodec<AppendEntryToLootPool> CODEC = RecordCodecBuilder.mapCodec(instance -> codecStart(instance)
            .and(instance.group(
                    SerializationUtils.singleOrSet(LootTable.KEY_CODEC, 1).fieldOf("tables").forGetter(ltm -> ltm.tables),
                    LootPoolEntries.CODEC.listOf(1, Integer.MAX_VALUE).fieldOf("entries").forGetter(ltm -> ltm.entries)
            )).apply(instance, AppendEntryToLootPool::new)
    );

    private final Set<ResourceKey<LootTable>> tables;
    private final List<LootPoolEntryContainer> entries;

    public AppendEntryToLootPool(Set<ResourceKey<LootTable>> tables, List<LootPoolEntryContainer> entries) {
        this(LootTableModifier.DEFAULT_PRIORITY, tables, entries);
    }

    public AppendEntryToLootPool(int priority, Set<ResourceKey<LootTable>> tables, List<LootPoolEntryContainer> entries) {
        super(priority);
        this.tables = tables;
        this.entries = entries;
    }

    @Override
    public LootTable modify(ResourceKey<LootTable> key, LootTable original) {
        // Skip if table not in list to modify
        if (!this.tables.contains(key)) return original;

        // Remove main pool
        var pool = original.removePool("main");
        if (pool == null) pool = original.removePool("pool0");

        // Readd with modified entries
        original.addPool(pool.ashsdimensions$mutableCopyOf().ashsdimensions$addAll(this.entries).build());
        return original;
    }

    @Override
    public MapCodec<? extends LootTableModifier> codec() {
        return CODEC;
    }
}
