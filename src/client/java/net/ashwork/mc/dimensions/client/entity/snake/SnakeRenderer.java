package net.ashwork.mc.dimensions.client.entity.snake;

import net.ashwork.mc.dimensions.client.entity.EntityRenderers;
import net.ashwork.mc.dimensions.entity.Snake;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

// TODO: Actually implement
public class SnakeRenderer extends MobRenderer<Snake, SnakeRenderState, SnakeModel<SnakeRenderState>> {

    public SnakeRenderer(EntityRendererProvider.Context context) {
        // TODO: Shadow will probably need to be replaced
        super(context, new SnakeModel<>(context.getModelSet().bakeLayer(EntityRenderers.SNAKE)), 0.2f);
    }

    @Override
    public Identifier getTextureLocation(SnakeRenderState state) {
        return null;
    }

    @Override
    public SnakeRenderState createRenderState() {
        return new SnakeRenderState();
    }
}
