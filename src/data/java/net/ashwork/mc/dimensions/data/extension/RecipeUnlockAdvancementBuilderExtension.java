package net.ashwork.mc.dimensions.data.extension;

import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.resources.Identifier;
import org.apache.commons.lang3.NotImplementedException;
import org.jspecify.annotations.Nullable;

public interface RecipeUnlockAdvancementBuilderExtension {

    default void ashsdimensions$requirements(AdvancementRequirements requirements) {
        throw new NotImplementedException();
    }

    default void ashsdimensions$useOROfANDs(@Nullable Identifier group) {
        throw new NotImplementedException();
    }
}
