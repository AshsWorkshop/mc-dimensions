package net.ashwork.mc.dimensions.data.mixin;

import net.ashwork.mc.dimensions.data.extension.RecipeBuilderExtension;
import net.ashwork.mc.dimensions.data.extension.RecipeUnlockAdvancementBuilderExtension;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ShapedRecipeBuilder.class)
public class ShapedRecipeBuilderMixin implements RecipeBuilderExtension {

    @Final
    @Shadow
    private RecipeUnlockAdvancementBuilder advancementBuilder;

    @Override
    public RecipeBuilder requirements(AdvancementRequirements requirements) {
        ((RecipeUnlockAdvancementBuilderExtension) this.advancementBuilder).requirements(requirements);
        return (ShapedRecipeBuilder) (Object) this;
    }
}
