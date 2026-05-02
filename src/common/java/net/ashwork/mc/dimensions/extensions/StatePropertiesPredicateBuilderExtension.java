package net.ashwork.mc.dimensions.extensions;

import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Optional;

public interface StatePropertiesPredicateBuilderExtension {

    StatePropertiesPredicate.Builder with(Property<?> property, StatePropertiesPredicate.ValueMatcher matcher);

    default StatePropertiesPredicate.Builder greaterOrEqual(Property<Integer> property, int min) {
        return this.with(property, new StatePropertiesPredicate.RangedMatcher(Optional.of(Integer.toString(min)), Optional.empty()));
    }

    default StatePropertiesPredicate.Builder lessOrEqual(Property<Integer> property, int max) {
        return this.with(property, new StatePropertiesPredicate.RangedMatcher(Optional.empty(), Optional.of(Integer.toString(max))));
    }

    default StatePropertiesPredicate.Builder between(Property<Integer> property, int min, int max) {
        return this.with(property, new StatePropertiesPredicate.RangedMatcher(Optional.of(Integer.toString(min)), Optional.of(Integer.toString(max))));
    }
}
