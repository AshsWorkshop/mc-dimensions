package net.ashwork.mc.dimensions.extension;

import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Predicate;

public interface AdvancementRequirementsExtension {

    default boolean testFlip(Predicate<String> predicate) {
        throw new NotImplementedException();
    }
}
