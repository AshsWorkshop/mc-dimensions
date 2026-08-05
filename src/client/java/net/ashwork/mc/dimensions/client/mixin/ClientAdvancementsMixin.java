package net.ashwork.mc.dimensions.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.ashwork.mc.dimensions.client.resources.ClientAdvancementRequirementsFlipper;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ClientAdvancements.class)
public class ClientAdvancementsMixin {

    @Inject(
            method = "update",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/AdvancementProgress;update(Lnet/minecraft/advancements/AdvancementRequirements;)V")
    )
    private void setAdvancementId(ClientboundUpdateAdvancementsPacket packet, CallbackInfo info, @Local Map.Entry<Identifier, AdvancementProgress> entry) {
        entry.getValue().ashsdimensions$setAdvancementId(entry.getKey());
        entry.getValue().ashsdimensions$setFlipRequirements(ClientAdvancementRequirementsFlipper.INSTANCE::shouldFlipRequirements);
    }
}
