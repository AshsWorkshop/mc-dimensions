package net.ashwork.mc.dimensions.data.server.datapack;

import net.ashwork.mc.dimensions.storage.enchantment.items.EnchantmentItemAppender;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.enchantment.Enchantments;

public interface EnchantmentItemAppenders {

    static void register(BootstrapContext<EnchantmentItemAppender> bootstrap) {
        var enchantments = bootstrap.lookup(Registries.ENCHANTMENT);
        var items = bootstrap.lookup(Registries.ITEM);

        bootstrap.register(
                IdUtils.key(EnchantmentItemAppender.REGISTRY_KEY, "fortune"), new EnchantmentItemAppender(
                        HolderSet.direct(enchantments.getOrThrow(Enchantments.FORTUNE)),
                        items.getOrThrow(DimensionItemTags.ENCHANTABLE_FORTUNE)
                )
        );
    }
}
