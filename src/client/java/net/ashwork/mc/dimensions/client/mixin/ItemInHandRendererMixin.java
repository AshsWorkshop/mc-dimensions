package net.ashwork.mc.dimensions.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.ashwork.mc.dimensions.client.neoext.ModelExtensions;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

        boolean isMainHand = mob.getItemInHand(InteractionHand.MAIN_HAND) == item;
        HumanoidArm arm = isMainHand ? mob.getMainArm() : mob.getMainArm().getOpposite();

        if (mob.getItemHeldByArm(arm.getOpposite()).is(DimensionItemTags.SIFTERS) && !(isMainHand && item.is(DimensionItemTags.SIFTERS))) {
            instance.updateForTopItem(
                    output, item, displayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND ? ModelExtensions.IN_SIFTER_FIRST_PERSON_RIGHTHAND : ModelExtensions.IN_SIFTER_FIRST_PERSON_LEFTHAND, level, owner, seed
            );
            return;
        }

        original.call(instance, output, item, displayContext, level, owner, seed);
    }
}
