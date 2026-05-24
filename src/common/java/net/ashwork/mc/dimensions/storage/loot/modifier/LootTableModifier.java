package net.ashwork.mc.dimensions.storage.loot.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Function;

public interface LootTableModifier {

    ResourceKey<Registry<MapCodec<? extends LootTableModifier>>> TYPE_KEY = IdUtils.registry("loot_table_modifier_type");
    Registry<MapCodec<? extends LootTableModifier>> TYPE_REGISTRY = new RegistryBuilder<>(TYPE_KEY).create();
    ResourceKey<Registry<LootTableModifier>> REGISTRY_KEY = IdUtils.registry("loot_table_modifier");

    Codec<LootTableModifier> DIRECT_CODEC = TYPE_REGISTRY.byNameCodec().dispatch(LootTableModifier::codec, Function.identity());

    int DEFAULT_PRIORITY = 1000;

    LootTable modify(ResourceKey<LootTable> key, LootTable original);

    MapCodec<? extends LootTableModifier> codec();

    int priority();
}
