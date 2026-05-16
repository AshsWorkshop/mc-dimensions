package net.ashwork.mc.dimensions.event;

import net.ashwork.mc.dimensions.resources.holderset.ModifiableOrHolderSet;
import net.ashwork.mc.dimensions.storage.enchantment.items.EnchantmentItemAppender;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.mixins.MappedRegistryAccessor;

import java.util.List;
import java.util.Optional;

public interface ServerEvents {

    static void addListeners() {
        NeoForge.EVENT_BUS.addListener(ServerEvents::applyEnchantmentModifiers);
    }

    static void applyEnchantmentModifiers(ServerAboutToStartEvent event) {
        var registries = event.getServer().registryAccess();

        // Apply enchantment modifiers
        List<EnchantmentItemAppender> enchantmentModifiers = registries.lookupOrThrow(EnchantmentItemAppender.REGISTRY_KEY)
                .listElements()
                .map(Holder::value)
                .toList();

        var enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
        // For each enchantment
        enchantments.listElements().forEach(enchantment -> {
            // Make sure supported items is modifiable
            var supportedItems = enchantment.value().definition().supportedItems();
            if (supportedItems instanceof ModifiableOrHolderSet<Item> modifiable) {
                boolean isModified = false;
                // For each modifier
                for (var modifier : enchantmentModifiers) {
                    // If enchantment in list of enchantments to append
                    if (modifier.enchantments().contains(enchantment)) {
                        // Append enchantments
                        isModified = true;
                        modifiable.add(modifier.supportedItems());
                    }
                }

                // If modified, force sync
                if (isModified) {
                    enchantments.registrationInfo(enchantment.key()).ifPresent(info -> {
                        RegistrationInfo newInfo = new RegistrationInfo(Optional.empty(), info.lifecycle());
                        ((MappedRegistryAccessor<Enchantment>) enchantments).neoforge$getRegistrationInfos().put(enchantment.key(), newInfo);
                    });
                }
            }
        });
    }
}
