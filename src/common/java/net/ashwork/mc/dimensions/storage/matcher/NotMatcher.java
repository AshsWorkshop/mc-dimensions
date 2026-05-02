package net.ashwork.mc.dimensions.storage.matcher;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;

public record NotMatcher(StatePropertiesPredicate.ValueMatcher matcher) implements DimensionValueMatcher {

    public static final MapCodec<NotMatcher> CODEC = StatePropertiesPredicate.ValueMatcher.CODEC.fieldOf("matcher")
            .xmap(NotMatcher::new, NotMatcher::matcher);

    @Override
    public <T extends Comparable<T>> boolean match(StateHolder<?, ?> state, Property<T> property) {
        return !this.matcher.match(state, property);
    }

    @Override
    public MapCodec<? extends DimensionValueMatcher> codec() {
        return CODEC;
    }
}
