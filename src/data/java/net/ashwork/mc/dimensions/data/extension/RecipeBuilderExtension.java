package net.ashwork.mc.dimensions.data.extension;

import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.Identifier;
import org.apache.commons.lang3.NotImplementedException;
import org.jspecify.annotations.Nullable;

public interface RecipeBuilderExtension<BUILDER extends RecipeBuilder> {

    default BUILDER requirements(AdvancementRequirements requirements) {
        throw new NotImplementedException();
    }

    default BUILDER useOROfANDs() {
        return this.useOROfANDs(null);
    }

    default BUILDER useOROfANDs(@Nullable Identifier group) {
        throw new NotImplementedException();
    }
}
