package net.ashwork.mc.dimensions.mixin;

import net.ashwork.mc.dimensions.extension.AdvancementProgressExtension;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.PlayerAdvancements;
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
        progress.setAdvancementId(holder.id());
    }
}
