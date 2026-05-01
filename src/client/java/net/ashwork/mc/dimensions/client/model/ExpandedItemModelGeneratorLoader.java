package net.ashwork.mc.dimensions.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class ExpandedItemModelGeneratorLoader implements UnbakedModelLoader<ExpandedItemModelGenerator> {

    public static final ExpandedItemModelGeneratorLoader INSTANCE = new ExpandedItemModelGeneratorLoader();
    public static final Identifier ID = IdUtils.id("item/generated_expanded");

    private static final String PLANE_DIMENSIONS_KEY = "plane";
    public static final Vector3fc DEFAULT_PLANE_DIMENSIONS = new Vector3f(16f, 16f, 1f);

    private ExpandedItemModelGeneratorLoader() {}

    @Override
    public ExpandedItemModelGenerator read(JsonObject obj, JsonDeserializationContext context) throws JsonParseException {
        // Set particle reference
        if (obj.has("textures")) {
            JsonObject textures = GsonHelper.getAsJsonObject(obj, "textures");
            if (!textures.has("particle")) textures.addProperty("particle", "#layer0");
        }

        // Read in standard model params
        StandardModelParameters params = StandardModelParameters.parse(obj, context);

        // Read in extended params
        Vector3fc plane = this.getPlaneDimensions(obj);

        return new ExpandedItemModelGenerator(params, plane);
    }

    private Vector3fc getPlaneDimensions(JsonObject obj) {
        if (!obj.has(PLANE_DIMENSIONS_KEY)) return DEFAULT_PLANE_DIMENSIONS;

        JsonObject plane = GsonHelper.getAsJsonObject(obj, PLANE_DIMENSIONS_KEY);
        var dimensions = new Vector3f(DEFAULT_PLANE_DIMENSIONS);
        if (plane.has("width")) dimensions.x = getAndValidateFloat(plane, "width");
        if (plane.has("height")) dimensions.y = getAndValidateFloat(plane, "height");
        if (plane.has("depth")) dimensions.z = getAndValidateFloat(plane, "depth");

        return dimensions;
    }

    private static float getAndValidateFloat(JsonObject plane, String name) {
        float dimension = GsonHelper.getAsFloat(plane, name);
        if (dimension <= 0 || dimension > 16) {
            throw new IllegalArgumentException("Dimension " + name + " must be in the range of (0,16]. Got '" + dimension + "'.");
        }
        return dimension;
    }
}
