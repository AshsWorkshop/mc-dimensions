package net.ashwork.mc.dimensions.data.server.datapack;

import net.ashwork.mc.dimensions.registry.DimensionDepositables;
import net.ashwork.mc.dimensions.storage.depositable.AtOrAboveHeightmap;
import net.ashwork.mc.dimensions.storage.depositable.DepositableTime;
import net.ashwork.mc.dimensions.storage.depositable.HasBlockState;
import net.ashwork.mc.dimensions.storage.enchantment.items.EnchantmentItemAppender;
import net.ashwork.mc.dimensions.storage.matcher.NotMatcher;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.advancements.criterion.BlockPredicate;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;

public interface DimensionsDatapackRegistries {

    static RegistrySetBuilder register(RegistrySetBuilder builder) {
        return builder.add(DepositableTime.Entry.REGISTRY_KEY, DepositTimes::register)
                .add(EnchantmentItemAppender.REGISTRY_KEY, EnchantmentItemAppenders::register)
                .add(Registries.BIOME, DimensionsBiomes::register)
                .add(Registries.DIMENSION_TYPE, DimensionsDimensionTypes::register)
                .add(Registries.LEVEL_STEM, DimensionsLevelStems::register);
    }
}
