package net.ashwork.mc.dimensions.registry;

import net.ashwork.mc.dimensions.storage.matcher.NotMatcher;

import static net.ashwork.mc.dimensions.registry.DimensionRegistrars.*;

public interface DimensionValueMatchers {

    static void register() {
        VALUE_MATCHER.register("not", () -> NotMatcher.CODEC);
    }
}
