package net.ashwork.mc.dimensions.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.ashwork.mc.dimensions.client.neoext.ModelExtensions;
import net.ashwork.mc.dimensions.client.neoext.item.SifterClientExtension;
import net.ashwork.mc.dimensions.storage.siftable.Siftable;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @WrapOperation(
            method = "renderItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemModelResolver;updateForTopItem(Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/ItemOwner;I)V")
    )
    private void switchDisplayContext(ItemModelResolver instance, ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, @Nullable Level level, @Nullable ItemOwner owner, int seed, Operation<Void> original) {
        var mob = owner.asLivingEntity();
        if (mob == null || !(displayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)) {
            original.call(instance, output, item, displayContext, level, owner, seed);
            return;
        }

        if (Siftable.maybeSift(mob)) {
            instance.updateForTopItem(
                    output, item, displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND ? ModelExtensions.IN_SIFTER_FIRST_PERSON_RIGHTHAND : ModelExtensions.IN_SIFTER_FIRST_PERSON_LEFTHAND, level, owner, seed
            );
            return;
        }

        original.call(instance, output, item, displayContext, level, owner, seed);
    }

    @WrapOperation(
            method = "renderArmWithItem",
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/extensions/common/IClientItemExtensions;applyForgeHandTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/entity/HumanoidArm;Lnet/minecraft/world/item/ItemStack;FFF)Z")
    )
    private boolean applyOffhandAnimation(IClientItemExtensions extensions, PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess, Operation<Boolean> original) {
        var oppositeHand = arm == player.getMainArm() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == oppositeHand && player.getItemInHand(oppositeHand).is(DimensionItemTags.SIFTERS)) {
            int animationTick = player.getUseItemRemainingTicks() % 10;
            int invert = arm == HumanoidArm.RIGHT ? 1 : -1;
            poseStack.translate(invert * 0.56F, -0.52F, -0.72F);
            poseStack.translate(Mth.lerp(partialTick, SifterClientExtension.sideToSideAnimationX(animationTick), SifterClientExtension.sideToSideAnimationX(animationTick - 1)), 0, 0);
            return true;
        }
        return original.call(extensions, poseStack, player, arm, itemInHand, partialTick, equipProcess, swingProcess);
    }
}
