package net.ashwork.mc.dimensions.registry;

import net.ashwork.mc.dimensions.storage.loot.provider.number.EnchantmentLevelOnToolProvider;

import static net.ashwork.mc.dimensions.registry.DimensionRegistrars.*;

public interface DimensionNumberProviders {

    static void register() {
        registerInstance(NUMBER_PROVIDER, "enchantment_level_on_tool", EnchantmentLevelOnToolProvider.MAP_CODEC);
    }
}
