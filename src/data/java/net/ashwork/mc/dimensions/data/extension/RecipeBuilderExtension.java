package net.ashwork.mc.dimensions.data.extension;

import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.Identifier;
import org.apache.commons.lang3.NotImplementedException;
import org.jspecify.annotations.Nullable;

public interface RecipeBuilderExtension<BUILDER extends RecipeBuilder> {

    default BUILDER ashsdimensions$requirements(AdvancementRequirements requirements) {
        throw new NotImplementedException();
    }

    default BUILDER ashsdimensions$useOROfANDs() {
        return this.ashsdimensions$useOROfANDs(null);
    }

    default BUILDER ashsdimensions$useOROfANDs(@Nullable Identifier group) {
        throw new NotImplementedException();
    }
}
