package net.ashwork.mc.dimensions.registry;

import net.ashwork.mc.dimensions.entity.Snake;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import java.util.function.Supplier;

import static net.ashwork.mc.dimensions.registry.DimensionRegistrars.*;

public interface DimensionEntities {

    Supplier<EntityType<Snake>> SNAKE = ENTITY.registerEntityType("snake", Snake::new, MobCategory.CREATURE);

    static void register(IEventBus modBus) {
        modBus.addListener(DimensionEntities::setAttributes);
    }

    private static void setAttributes(EntityAttributeCreationEvent event) {
        event.put(SNAKE.get(), Snake.createAttributes().build());
    }
}
