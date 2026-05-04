package net.ashwork.mc.dimensions.extension;

import java.util.function.Predicate;

public interface AdvancementRequirementsExtension {

    boolean testFlip(Predicate<String> predicate);
}
