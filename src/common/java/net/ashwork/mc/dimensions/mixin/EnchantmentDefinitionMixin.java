package net.ashwork.mc.dimensions.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.MapCodec;
import net.ashwork.mc.dimensions.resources.holderset.ModifiableOrHolderSet;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Mixin(value = Enchantment.EnchantmentDefinition.class, priority = 100_000)
public class EnchantmentDefinitionMixin {

    @Shadow
    @Final
    public static MapCodec<Enchantment.EnchantmentDefinition> CODEC;

    static {
        CODEC = CODEC.xmap(def -> {
            ModifiableOrHolderSet<Item> modifiable = new ModifiableOrHolderSet<>(def.supportedItems());
            return new Enchantment.EnchantmentDefinition(
                    modifiable, def.primaryItems(), def.weight(), def.maxLevel(), def.minCost(), def.maxCost(), def.anvilCost(), def.slots()
            );
        }, Function.identity());
    }
}
