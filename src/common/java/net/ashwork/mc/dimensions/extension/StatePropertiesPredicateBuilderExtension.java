package net.ashwork.mc.dimensions.extension;

import net.ashwork.mc.dimensions.util.ClassUtils;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Optional;

public interface StatePropertiesPredicateBuilderExtension {

    default StatePropertiesPredicate.Builder ashsdimensions$with(Property<?> property, StatePropertiesPredicate.ValueMatcher matcher) {
        return ClassUtils.extensionDefault();
    }

    default StatePropertiesPredicate.Builder ashsdimensions$greaterOrEqual(Property<Integer> property, int min) {
        return this.ashsdimensions$with(property, new StatePropertiesPredicate.RangedMatcher(Optional.of(Integer.toString(min)), Optional.empty()));
    }

    default StatePropertiesPredicate.Builder ashsdimensions$lessOrEqual(Property<Integer> property, int max) {
        return this.ashsdimensions$with(property, new StatePropertiesPredicate.RangedMatcher(Optional.empty(), Optional.of(Integer.toString(max))));
    }

    default StatePropertiesPredicate.Builder ashsdimensions$between(Property<Integer> property, int min, int max) {
        return this.ashsdimensions$with(property, new StatePropertiesPredicate.RangedMatcher(Optional.of(Integer.toString(min)), Optional.of(Integer.toString(max))));
    }
}
