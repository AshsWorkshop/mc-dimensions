package net.ashwork.mc.dimensions.data.extension;

import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public interface RecipeUnlockAdvancementBuilderExtension {

    void requirements(AdvancementRequirements requirements);

    void useOROfANDs(@Nullable Identifier group);
}
