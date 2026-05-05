package net.ashwork.mc.dimensions.data.mixin;

import net.ashwork.mc.dimensions.data.extension.RecipeBuilderExtension;
import net.ashwork.mc.dimensions.data.extension.RecipeUnlockAdvancementBuilderExtension;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ShapedRecipeBuilder.class)
public class ShapedRecipeBuilderMixin implements RecipeBuilderExtension<ShapedRecipeBuilder> {

    @Final
    @Shadow
    private RecipeUnlockAdvancementBuilder advancementBuilder;

    @Override
    public ShapedRecipeBuilder requirements(AdvancementRequirements requirements) {
        this.advancementBuilder.requirements(requirements);
        return (ShapedRecipeBuilder) (Object) this;
    }

    @Override
    public ShapedRecipeBuilder useOROfANDs(@Nullable Identifier group) {
        this.advancementBuilder.useOROfANDs(group);
        return (ShapedRecipeBuilder) (Object) this;
    }
}
