package net.ashwork.mc.dimensions.entity;

import net.ashwork.mc.dimensions.registry.DimensionEntities;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

// TODO: Actually implement
// Want to make each segment of the snake trail behind dynamically rather than an animation
public class Snake extends Animal {

    public Snake(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes();
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(DimensionItemTags.FOOD_SNAKE);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return DimensionEntities.SNAKE.get().create(level, EntitySpawnReason.BREEDING);
    }
}
