package net.ashwork.mc.dimensions.extension;

import net.ashwork.mc.dimensions.util.ClassUtils;

import java.util.function.Predicate;

public interface AdvancementRequirementsExtension {

    default boolean ashsdimensions$testFlip(Predicate<String> predicate) {
        return ClassUtils.extensionDefault();
    }
}
