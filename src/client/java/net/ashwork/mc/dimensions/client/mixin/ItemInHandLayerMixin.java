package net.ashwork.mc.dimensions.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.ashwork.mc.dimensions.client.neoext.ModelExtensions;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin<S extends ArmedEntityRenderState, M extends EntityModel<S> & ArmedModel<S>> extends RenderLayer<S, M> {

    private ItemInHandLayerMixin(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }

    @Shadow
    protected abstract boolean useBabyOffset(ArmedEntityRenderState state);

    @Inject(
            method = "submitArmWithItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void submitSifter(ArmedEntityRenderState state, ItemStackRenderState item, ItemStack itemStack, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo info) {
        var oppositePose = arm == HumanoidArm.LEFT ? state.rightArmPose : state.leftArmPose;

        // If the opposite hand contains a sifter
        if (oppositePose == ModelExtensions.SIFTER_POSE.getValue()) {
            // Validate not sifters and has custom rendering
            if (itemStack.is(DimensionItemTags.SIFTERS) || ModelExtensions.sifterItem((ArmedModel<ArmedEntityRenderState>) this.getParentModel(), this::useBabyOffset, state, item, itemStack, arm, poseStack, submitNodeCollector, lightCoords)) {
                info.cancel();
                return;
            }
        }
    }
}
