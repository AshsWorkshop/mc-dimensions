package net.ashwork.mc.dimensions.mixin;

import net.ashwork.mc.dimensions.resources.advancement.AdvancementRequirementsFlipper;
import net.ashwork.mc.dimensions.resources.advancement.AdvancementRequirementsFlipperListener;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.PlayerAdvancements;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {

    @Inject(
            method = "startProgress",
            at = @At("HEAD")
    )
    private void setAdvancementId(AdvancementHolder holder, AdvancementProgress progress, CallbackInfo info) {
        progress.ashsdimensions$setAdvancementId(holder.id());
        progress.ashsdimensions$setFlipRequirements(ServerLifecycleHooks.getCurrentServer().getServerResources().managers().getListener(AdvancementRequirementsFlipperListener.ID)::shouldFlipRequirements);
    }
}
