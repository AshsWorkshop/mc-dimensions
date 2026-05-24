package net.ashwork.mc.dimensions.extension;

import net.ashwork.mc.dimensions.util.ClassUtils;
import net.minecraft.resources.Identifier;

public interface AdvancementProgressExtension {

    default void ashsdimensions$setAdvancementId(Identifier advancement) {
        ClassUtils.extensionDefault();
    }
}
