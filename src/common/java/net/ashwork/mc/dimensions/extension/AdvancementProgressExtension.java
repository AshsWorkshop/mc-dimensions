package net.ashwork.mc.dimensions.extension;

import net.minecraft.resources.Identifier;
import org.apache.commons.lang3.NotImplementedException;

public interface AdvancementProgressExtension {

    default void ashsdimensions$setAdvancementId(Identifier advancement) {
        throw new NotImplementedException();
    }
}
