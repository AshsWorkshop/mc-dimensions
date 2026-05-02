package net.ashwork.mc.dimensions.storage.depositable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.Optional;

public record Depositable(Optional<Block> deposited, Optional<String> cycleProperty) {
    private static final Codec<Depositable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf("deposited").forGetter(Depositable::deposited),
            Codec.STRING.optionalFieldOf("cycle_property").forGetter(Depositable::cycleProperty)
    ).apply(instance, Depositable::new));

    public static final DataMapType<Block, Depositable> DATA_MAP = DataMapType.builder(IdUtils.id("depositable"), Registries.BLOCK, CODEC).build();

    public BlockState compute(BlockState state) {
        // First attempt to cycle property
        var propOpt = this.cycleProperty.map(name -> state.getBlock().getStateDefinition().getProperty(name));
        if (propOpt.isPresent()) {
            var property = propOpt.get();
            BlockState cycled = state.cycle(property);
            // If the cycle does not equal the first property value, return the new state
            if (cycled.getValue(property) != property.getPossibleValues().getFirst()) return cycled;
        }

        // Otherwise compute deposited blockstate
        var depositOpt = this.deposited.map(block -> {
            BlockState deposited = block.defaultBlockState();
            for (var property : state.getBlock().getStateDefinition().getProperties()) {
                deposited = trySetValue(deposited, state, property);
            }
            return deposited;
        });
        if (depositOpt.isPresent()) {
            return depositOpt.get();
        }

        // If neither are present, return the original state
        return state;
    }

    private static <T extends Comparable<T>> BlockState trySetValue(BlockState state, BlockState other, Property<T> property) {
        return state.trySetValue(property, other.getValue(property));
    }

    public static Builder accepts() {
        return new Builder();
    }

    public static class Builder {
        private Optional<Block> deposited;
        private Optional<String> cycleProperty;

        private Builder() {
            this.deposited = Optional.empty();
            this.cycleProperty = Optional.empty();
        }

        public Builder depositsTo(Block block) {
            this.deposited = Optional.of(block);
            return this;
        }

        public Builder cyclesThrough(Property<?> property) {
            this.cycleProperty = Optional.of(property.getName());
            return this;
        }

        public Depositable create() {
            if (this.deposited.isEmpty() && this.cycleProperty.isEmpty()) {
                throw new IllegalStateException("The depositable entry must have either its deposited block or a property to cycle.");
            }
            return new Depositable(this.deposited, this.cycleProperty);
        }
    }
}
