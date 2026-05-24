package net.ashwork.mc.dimensions.data.extension;

import net.ashwork.mc.dimensions.util.ClassUtils;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public interface RecipeBuilderExtension<BUILDER extends RecipeBuilder> {

    default BUILDER ashsdimensions$requirements(AdvancementRequirements requirements) {
        return ClassUtils.extensionDefault();
    }

    default BUILDER ashsdimensions$useOROfANDs() {
        return this.ashsdimensions$useOROfANDs(null);
    }

    default BUILDER ashsdimensions$useOROfANDs(@Nullable Identifier group) {
        return ClassUtils.extensionDefault();
    }
}
