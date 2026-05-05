package net.ashwork.mc.dimensions.client.neoext;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.ashwork.mc.dimensions.client.AshsDimensionsClient;
import net.ashwork.mc.dimensions.registry.DimensionItems;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.effects.SpearAnimations;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwingAnimationType;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

public interface ModelExtensions {
    EnumProxy<HumanoidModel.ArmPose> SIFTER_POSE = new EnumProxy<>(
            HumanoidModel.ArmPose.class, true, true, (IArmPoseTransformer) ModelExtensions::sifterPose
    );

    private static void sifterPose(HumanoidModel<?> model, HumanoidRenderState renderState, HumanoidArm arm) {
        model.rightArm.xRot = -2 * Mth.PI / 6;
        model.leftArm.xRot = -2 * Mth.PI / 6;
    }

    static boolean sifterItem(ArmedModel<ArmedEntityRenderState> model, Predicate<ArmedEntityRenderState> useBabyOffset, ArmedEntityRenderState state, ItemStackRenderState item, ItemStack itemStack, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
        // TODO: Baby offset
        if (item.usesBlockLight()) {
            poseStack.pushPose();
            model.translateToHand(state, arm, poseStack);
            boolean isLeftHand = arm == HumanoidArm.LEFT;
            poseStack.mulPose(Axis.XP.rotationDegrees(-45.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees((isLeftHand ? -1 : 1) * 45.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees((isLeftHand ? 1 : -1) * 192.5F));
            poseStack.translate((isLeftHand ? 1 : -1) * 9 / 16f, 0 / 16f, -4 / 16f);

            item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
            return true;
        }

        return false;
    }

    static void setup() {
        NeoForge.EVENT_BUS.addListener(ModelExtensions::renderHand);
    }

    private static void renderHand(RenderHandEvent event) {
        var player = Minecraft.getInstance().player;
        if (
                // If the opposite hand contains a sifter
                getItemInOpposite(player, event.getHand()).is(DimensionItemTags.SIFTERS)
                        // And the main hand does not already contain a sifter
                        && !(event.getHand() == InteractionHand.MAIN_HAND && player.getItemInHand(event.getHand()).is(DimensionItemTags.SIFTERS))
        ) {
            // Cancel rendering
            event.setCanceled(true);
        }
    }

    private static ItemStack getItemInOpposite(Player player, InteractionHand hand) {
        hand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        return player.getItemInHand(hand);
    }
}
