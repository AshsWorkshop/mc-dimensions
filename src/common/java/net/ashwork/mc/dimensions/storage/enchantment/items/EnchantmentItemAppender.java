package net.ashwork.mc.dimensions.storage.enchantment.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public record EnchantmentItemAppender(HolderSet<Enchantment> enchantments, HolderSet<Item> supportedItems) {
    public static final Codec<EnchantmentItemAppender> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.ENCHANTMENT).fieldOf("enchantments").forGetter(EnchantmentItemAppender::enchantments),
            RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("supported_items").forGetter(EnchantmentItemAppender::supportedItems)
    ).apply(instance, EnchantmentItemAppender::new));

    public static final ResourceKey<Registry<EnchantmentItemAppender>> REGISTRY_KEY = IdUtils.registry("enchantment_item_appender");
}
