package net.ashwork.mc.dimensions.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.ashwork.mc.dimensions.resources.holderset.LazyDelegateHolderSet;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.HolderSetCodec;
import net.neoforged.neoforge.registries.holdersets.ICustomHolderSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(HolderSetCodec.class)
public class HolderSetCodecMixin {

    @ModifyVariable(
            method = "encode(Lnet/minecraft/core/HolderSet;Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
            at = @At("HEAD"),
            argsOnly = true,
            name = "input"
    )
    private HolderSet resolveWrapper(HolderSet input) {
        return input instanceof LazyDelegateHolderSet lazy ? lazy.toSet() : input;
    }
}
