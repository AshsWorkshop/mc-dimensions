package net.ashwork.mc.dimensions.data.extension;

import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public interface RecipeBuilderExtension {

    RecipeBuilder requirements(AdvancementRequirements requirements);

    default void useOROfANDs() {
        this.useOROfANDs(null);
    }

    RecipeBuilder useOROfANDs(@Nullable Identifier group);
}
