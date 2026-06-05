package net.ashwork.mc.dimensions.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.ashwork.mc.dimensions.client.neoext.ModelExtensions;
import net.ashwork.mc.dimensions.storage.siftable.Siftable;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmedEntityRenderState.class)
public class ArmedEntityRenderStateMixin {

    @Unique
    private static ArmedEntityRenderState renderState;

    @Inject(
            method = "extractArmedEntityRenderState",
            at = @At("HEAD")
    )
    private static void capture(LivingEntity entity, ArmedEntityRenderState state, ItemModelResolver itemModelResolver, float partialTicks, CallbackInfo info) {
        renderState = state;
    }

    @Inject(
            method = "extractArmedEntityRenderState",
            at = @At("TAIL")
    )
    private static void release(LivingEntity entity, ArmedEntityRenderState state, ItemModelResolver itemModelResolver, float partialTicks, CallbackInfo info) {
        renderState = null;
    }

    @WrapOperation(
            method = "extractArmedEntityRenderState",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemModelResolver;updateForLiving(Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/entity/LivingEntity;)V")
    )
    private static void switchDisplayContext(ItemModelResolver instance, ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, LivingEntity entity, Operation<Void> original) {
        if (!(displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)) {
            original.call(instance, output, item, displayContext, entity);
            return;
        }

        if (Siftable.maybeSift(entity)) {
            instance.updateForLiving(
                    output, item, ModelExtensions.sifterThirdPersonDisplay(displayContext, entity), entity
            );
            return;
        }

        original.call(instance, output, item, displayContext, entity);
    }
}
