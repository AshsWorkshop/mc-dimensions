package net.ashwork.mc.dimensions.client.entity;

import net.ashwork.mc.dimensions.client.entity.snake.SnakeModel;
import net.ashwork.mc.dimensions.client.entity.snake.SnakeRenderer;
import net.ashwork.mc.dimensions.registry.DimensionEntities;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public interface EntityRenderers {

    ModelLayerLocation SNAKE = main("snake");

    static void register(IEventBus modBus) {
        modBus.addListener(EntityRenderers::layerDefinitions);
        modBus.addListener(EntityRenderers::entityRenderers);
    }

    private static void layerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SNAKE, SnakeModel::createLayer);
    }

    private static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(DimensionEntities.SNAKE.get(), SnakeRenderer::new);
    }

    private static ModelLayerLocation main(String name) {
        return new ModelLayerLocation(IdUtils.id(name), "main");
    }
}
