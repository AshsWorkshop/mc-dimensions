package net.ashwork.mc.dimensions.util;

import org.apache.commons.lang3.NotImplementedException;

public interface ClassUtils {

    static void doNotInstantiate(Object object) throws IllegalStateException {
        throw new IllegalStateException(object.getClass().getSimpleName() + " should not be instantiated");
    }

    static <T> T extensionDefault() throws NotImplementedException {
        throw new NotImplementedException("This method should be implemented by it attached subtype.");
    }
}
