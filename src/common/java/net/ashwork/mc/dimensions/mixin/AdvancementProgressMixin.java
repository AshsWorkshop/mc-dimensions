package net.ashwork.mc.dimensions.mixin;

import net.ashwork.mc.dimensions.extension.AdvancementProgressExtension;
import net.ashwork.mc.dimensions.extension.AdvancementRequirementsExtension;
import net.ashwork.mc.dimensions.resources.AdvancementRequirementsFlipper;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AdvancementProgress.class)
public abstract class AdvancementProgressMixin implements AdvancementProgressExtension {

    @Nullable
    @Unique
    private Identifier advancementId;

    @Shadow
    private AdvancementRequirements requirements;
    @Shadow
    abstract boolean isCriterionDone(String criterion);

    @Override
    public void setAdvancementId(Identifier advancement) {
        this.advancementId = advancement;
    }

    @Inject(
            method = "isDone",
            at = @At("HEAD"),
            cancellable = true
    )
    private void checkFlip(CallbackInfoReturnable<Boolean> info) {
        if (AdvancementRequirementsFlipper.INSTANCE.shouldFlipRequirements(this.advancementId)) {
            info.setReturnValue(this.requirements.testFlip(this::isCriterionDone));
        }
    }
}
