package net.ashwork.mc.dimensions.mixin;

import com.google.common.collect.ImmutableList;
import net.ashwork.mc.dimensions.extension.StatePropertiesPredicateBuilderExtension;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StatePropertiesPredicate.Builder.class)
public class StatePropertiesPredicateBuilderMixin implements StatePropertiesPredicateBuilderExtension {

    @Final
    @Shadow
    private ImmutableList.Builder<StatePropertiesPredicate.PropertyMatcher> matchers;

    @Override
    public StatePropertiesPredicate.Builder with(Property<?> property, StatePropertiesPredicate.ValueMatcher matcher) {
        this.matchers.add(new StatePropertiesPredicate.PropertyMatcher(property.getName(), matcher));
        return (StatePropertiesPredicate.Builder) (Object) this;
    }
}
