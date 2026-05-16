package net.ashwork.mc.dimensions.registry;

import net.ashwork.mc.dimensions.storage.enchantment.items.EnchantmentItemAppender;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public interface DimensionEnchantments {

    static void datapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(EnchantmentItemAppender.REGISTRY_KEY, EnchantmentItemAppender.DIRECT_CODEC);
    }
}
