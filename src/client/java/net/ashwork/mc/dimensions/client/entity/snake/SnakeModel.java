package net.ashwork.mc.dimensions.client.entity.snake;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

// TODO: Actually implement
public class SnakeModel<T extends SnakeRenderState> extends EntityModel<T> {



    public SnakeModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        return LayerDefinition.create(createMesh(), 64, 32);
    }

    protected static MeshDefinition createMesh() {
        return new MeshDefinition();
    }
}
