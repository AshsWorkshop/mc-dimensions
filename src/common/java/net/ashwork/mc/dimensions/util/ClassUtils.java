package net.ashwork.mc.dimensions.util;

public interface ClassUtils {

    static void doNotInstantiate(Object object) throws IllegalStateException {
        throw new IllegalStateException(object.getClass().getSimpleName() + " should not be instantiated");
    }
}
