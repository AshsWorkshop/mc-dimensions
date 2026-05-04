package net.ashwork.mc.dimensions.data.extension;

import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.data.recipes.RecipeBuilder;

public interface RecipeBuilderExtension {

    RecipeBuilder requirements(AdvancementRequirements requirements);
}
