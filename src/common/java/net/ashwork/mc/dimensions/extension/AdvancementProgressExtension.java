package net.ashwork.mc.dimensions.extension;

import net.ashwork.mc.dimensions.util.ClassUtils;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.function.Predicate;

public interface AdvancementProgressExtension {

    default void ashsdimensions$setAdvancementId(Identifier advancement) {
        ClassUtils.extensionDefault();
    }

    default void ashsdimensions$setFlipRequirements(Predicate<@Nullable Identifier> flipRequirements) {
        ClassUtils.extensionDefault();
    }
}
