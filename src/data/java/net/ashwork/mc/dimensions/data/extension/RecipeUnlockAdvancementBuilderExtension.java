package net.ashwork.mc.dimensions.data.extension;

import net.ashwork.mc.dimensions.util.ClassUtils;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public interface RecipeUnlockAdvancementBuilderExtension {

    default void ashsdimensions$requirements(AdvancementRequirements requirements) {
        ClassUtils.extensionDefault();
    }

    default void ashsdimensions$useOROfANDs(@Nullable Identifier group) {
        ClassUtils.extensionDefault();
    }
}
