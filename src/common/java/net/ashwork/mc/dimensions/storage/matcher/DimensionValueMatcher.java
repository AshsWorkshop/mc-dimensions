package net.ashwork.mc.dimensions.storage.matcher;

import com.mojang.serialization.MapCodec;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public interface DimensionValueMatcher extends StatePropertiesPredicate.ValueMatcher {

    ResourceKey<Registry<MapCodec<? extends DimensionValueMatcher>>> TYPE_KEY = IdUtils.registry("value_matcher_type");
    Registry<MapCodec<? extends DimensionValueMatcher>> TYPE_REGISTRY = new RegistryBuilder<>(TYPE_KEY).create();

    MapCodec<? extends DimensionValueMatcher> codec();
}
