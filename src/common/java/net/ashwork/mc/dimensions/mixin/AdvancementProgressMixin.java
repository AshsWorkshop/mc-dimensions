package net.ashwork.mc.dimensions.mixin;

import net.ashwork.mc.dimensions.extension.AdvancementProgressExtension;
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

import java.util.function.Predicate;

@Mixin(AdvancementProgress.class)
public abstract class AdvancementProgressMixin implements AdvancementProgressExtension {

    @Nullable
    @Unique
    private Identifier advancementId;
    @Nullable
    @Unique
    private Predicate<@Nullable Identifier> flipRequirements;

    @Shadow
    private AdvancementRequirements requirements;
    @Shadow
    abstract boolean isCriterionDone(String criterion);

    @Override
    public void ashsdimensions$setAdvancementId(Identifier advancement) {
        this.advancementId = advancement;
    }

    @Override
    public void ashsdimensions$setFlipRequirements(Predicate<@Nullable Identifier> flipRequirements) {
        this.flipRequirements = flipRequirements;
    }

    @Inject(
            method = "isDone",
            at = @At("HEAD"),
            cancellable = true
    )
    private void checkFlip(CallbackInfoReturnable<Boolean> info) {
        if (this.flipRequirements != null && this.flipRequirements.test(this.advancementId)) {
            info.setReturnValue(this.requirements.ashsdimensions$testFlip(this::isCriterionDone));
        }
    }
}
