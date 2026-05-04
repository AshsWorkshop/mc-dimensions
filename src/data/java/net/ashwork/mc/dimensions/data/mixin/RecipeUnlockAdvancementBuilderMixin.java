package net.ashwork.mc.dimensions.data.mixin;

import net.ashwork.mc.dimensions.data.extension.RecipeUnlockAdvancementBuilderExtension;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RecipeUnlockAdvancementBuilder.class)
public class RecipeUnlockAdvancementBuilderMixin implements RecipeUnlockAdvancementBuilderExtension {

    @Nullable
    @Unique
    private AdvancementRequirements requirements;

    @Override
    public void requirements(AdvancementRequirements requirements) {
        this.requirements = requirements;
    }

    @Inject(
            method = "build(Lnet/minecraft/data/recipes/RecipeOutput;Lnet/minecraft/resources/ResourceKey;Ljava/lang/String;)Lnet/minecraft/advancements/AdvancementHolder;",
            at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void setRequirements(RecipeOutput output, ResourceKey<Recipe<?>> id, @org.jspecify.annotations.Nullable String category, CallbackInfoReturnable<AdvancementHolder> info, Advancement.Builder advancement) {
        if (this.requirements != null) {
            advancement.requirements(this.requirements);
        }
    }
}
