package net.ashwork.mc.dimensions.client.neoext.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.ashwork.mc.dimensions.client.neoext.ModelExtensions;
import net.ashwork.mc.dimensions.neoext.ItemExtensions;
import net.ashwork.mc.dimensions.storage.siftable.Siftable;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jspecify.annotations.Nullable;

public record SifterClientExtension() implements IClientItemExtensions {

    public static final SifterClientExtension INSTANCE = new SifterClientExtension();

    @Override
    public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
        return Siftable.maybeSift(entityLiving) ? ModelExtensions.SIFTER_POSE.getValue()
                : IClientItemExtensions.super.getArmPose(entityLiving, hand, itemStack);
    }

    @Override
    public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
        var hand = arm == player.getMainArm() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == hand && itemInHand.getUseAnimation() == ItemExtensions.USE_SIFTER) {
            int invert = arm == HumanoidArm.RIGHT ? 1 : -1;
            poseStack.translate(invert * 0.56F, -0.52F, -0.72F);
            poseStack.translate(Mth.lerp(partialTick, sideToSideAnimationX(player.getUseItemRemainingTicks()), sideToSideAnimationX(player.getUseItemRemainingTicks() - 1)), 0, 0);
            return true;
        }
        return false;
    }

    public static float sideToSideAnimationX(int animationTick) {
        return animation(animationTick, 10, 0.25f);
//        boolean firstPhase = animationTick < 5;
//        return (firstPhase ? 1f : -1f) * (Mth.abs(0.1f * (animationTick - (firstPhase ? 2.5f : 7.5f))) - 0.25f);
    }

    public static float animation(float animationTick, int period, float range) {
        animationTick = animationTick % period;
        float slice = (period / 4f);
        float cycle = (period / 2f);
        boolean firstPhase = animationTick < cycle;
        return (firstPhase ? 1f : -1f) * (Mth.abs((range / slice) * (animationTick - (firstPhase ? slice : (slice + cycle)))) - range);
    }
}
