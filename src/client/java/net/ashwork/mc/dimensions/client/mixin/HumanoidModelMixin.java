package net.ashwork.mc.dimensions.client.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.ashwork.mc.dimensions.client.neoext.ModelExtensions;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {

    @Shadow
    @Final
    private ModelPart leftArm;
    @Shadow
    @Final
    public ModelPart rightArm;

    @Shadow
    public abstract ModelPart getArm(HumanoidArm arm);

    @Unique
    private HumanoidRenderState renderStateCapture;
    @Unique
    private boolean bobAlreadyApplied = false;

    @Inject(
            method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
            at = @At("HEAD")
    )
    private void captureArms(HumanoidRenderState renderState, CallbackInfo ci) {
        this.renderStateCapture = renderState;
        this.bobAlreadyApplied = false;
    }

    @Inject(
            method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
            at = @At("TAIL")
    )
    private void releaseArms(HumanoidRenderState renderState, CallbackInfo ci) {
        this.renderStateCapture = null;
        this.bobAlreadyApplied = false;
    }

    @Definition(id = "SPYGLASS", field = "Lnet/minecraft/client/model/HumanoidModel$ArmPose;SPYGLASS:Lnet/minecraft/client/model/HumanoidModel$ArmPose;")
    @Expression("? != SPYGLASS")
    @WrapOperation(
            method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private boolean modifyBob(Object pose, Object spyglass, Operation<Boolean> original) {
        // See if we applied a pose
        if (this.bobAlreadyApplied) {
            return false;
        }

        // Check if its sifter on either hand
        if (
                this.renderStateCapture.leftArmPose == ModelExtensions.SIFTER_POSE.getValue()
                || this.renderStateCapture.rightArmPose == ModelExtensions.SIFTER_POSE.getValue()
        ) {
            this.bobAlreadyApplied = true;
            this.rightArm.xRot = this.rightArm.xRot + (Mth.sin(this.renderStateCapture.ageInTicks * 0.067F) * 0.05F);
            this.leftArm.xRot = this.leftArm.xRot + (Mth.sin(this.renderStateCapture.ageInTicks * 0.067F) * 0.05F);
            return false;
        }
        return original.call(pose, spyglass);
    }

    @Inject(
            method = "setupAttackAnimation",
            at = @At("TAIL")
    )
    private void modifyAttack(HumanoidRenderState renderState, CallbackInfo ci) {
        // Check if its sifter on either hand
        if (
                renderState.leftArmPose == ModelExtensions.SIFTER_POSE.getValue()
                        || renderState.rightArmPose == ModelExtensions.SIFTER_POSE.getValue()
        ) {
            if (renderState.attackTime > 0f) {
                this.getArm(renderState.attackArm.getOpposite()).xRot = this.getArm(renderState.attackArm).xRot;
                this.leftArm.yRot = 0;
                this.rightArm.yRot = 0;
                this.leftArm.zRot = 0;
                this.rightArm.zRot = 0;
            }
        }
    }
}
