package net.ashwork.mc.dimensions.data.client.model;

import com.google.gson.JsonObject;
import net.ashwork.mc.dimensions.client.model.ExpandedItemModelGeneratorLoader;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.generators.template.CustomLoaderBuilder;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class ExpandedItemModelGeneratorBuilder extends CustomLoaderBuilder {

    private Vector3f plane;

    public ExpandedItemModelGeneratorBuilder() {
        super(ExpandedItemModelGeneratorLoader.ID, false);
        this.plane = new Vector3f(ExpandedItemModelGeneratorLoader.DEFAULT_PLANE_DIMENSIONS);
    }

    public ExpandedItemModelGeneratorBuilder size(float size) {
        return this.size(size, size);
    }

    public ExpandedItemModelGeneratorBuilder size(float width, float height) {
        this.plane.x = width;
        this.plane.y = height;
        return this;
    }

    public ExpandedItemModelGeneratorBuilder depth(float depth) {
        this.plane.z = depth;
        return this;
    }

    @Override
    protected CustomLoaderBuilder copyInternal() {
        var builder = new ExpandedItemModelGeneratorBuilder();
        builder.plane = new Vector3f(this.plane);
        return builder;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        super.toJson(json);
        if (this.plane.equals(ExpandedItemModelGeneratorLoader.DEFAULT_PLANE_DIMENSIONS)) return json;

        var planeJson = new JsonObject();
        if (this.plane.x != ExpandedItemModelGeneratorLoader.DEFAULT_PLANE_DIMENSIONS.x()) planeJson.addProperty("width", this.plane.x);
        if (this.plane.y != ExpandedItemModelGeneratorLoader.DEFAULT_PLANE_DIMENSIONS.y()) planeJson.addProperty("height", this.plane.y);
        if (this.plane.z != ExpandedItemModelGeneratorLoader.DEFAULT_PLANE_DIMENSIONS.z()) planeJson.addProperty("depth", this.plane.z);

        json.add("plane", planeJson);
        return json;
    }
}
