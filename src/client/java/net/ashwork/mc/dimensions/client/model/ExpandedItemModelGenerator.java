package net.ashwork.mc.dimensions.client.model;

import com.mojang.math.Quadrant;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.FaceBakery;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @see net.minecraft.client.resources.model.cuboid.ItemModelGenerator
 */
public class ExpandedItemModelGenerator extends AbstractUnbakedModel {

    private static final List<String> LAYERS = List.of("layer0", "layer1", "layer2", "layer3", "layer4");
    private static final float UV_SHRINK = 0.1F;

    private final Vector3fc plane;

    public ExpandedItemModelGenerator(StandardModelParameters parameters, Vector3fc plane) {
        super(parameters);
        this.plane = plane;
    }

    @Override
    public @Nullable UnbakedGeometry geometry() {
        return this::bake;
    }

    @Override
    public @Nullable GuiLight guiLight() {
        var light = super.guiLight();
        return light != null ? light : GuiLight.FRONT;
    }

    private QuadCollection bake(TextureSlots textureSlots, ModelBaker modelBaker, ModelState modelState, ModelDebugName name) {
        QuadCollection singleResult = null;
        QuadCollection.Builder builder = null;

        for (int layerIndex = 0; layerIndex < LAYERS.size(); layerIndex++) {
            String textureReference = LAYERS.get(layerIndex);
            Material material = textureSlots.getMaterial(textureReference);
            if (material == null) {
                break;
            }

            Material.Baked bakedMaterial = modelBaker.materials().get(material, name);
            QuadCollection bakedLayer = modelBaker.compute(new ExpandedItemModelGenerator.ItemLayerKey(bakedMaterial, modelState, this.plane, layerIndex));
            if (builder != null) {
                builder.addAll(bakedLayer);
            } else if (singleResult != null) {
                builder = new QuadCollection.Builder();
                builder.addAll(singleResult);
                builder.addAll(bakedLayer);
                singleResult = null;
            } else {
                singleResult = bakedLayer;
            }
        }

        if (builder != null) {
            return builder.build();
        } else {
            return singleResult != null ? singleResult : QuadCollection.EMPTY;
        }
    }

    private static float minDimension(float value) {
        return (16f - value) * 0.5f;
    }

    private static float maxDimension(float value) {
        return (16f + value) * 0.5f;
    }

    private static void bakeExtrudedSprite(
            QuadCollection.Builder builder, ModelBaker.Interner interner, ModelState modelState, Vector3fc plane, BakedQuad.MaterialInfo materialInfo
    ) {
        Vector3f from = new Vector3f(minDimension(plane.x()), minDimension(plane.y()), minDimension(plane.z()));
        Vector3f to = new Vector3f(maxDimension(plane.x()), maxDimension(plane.y()), maxDimension(plane.z()));
        CuboidFace.UVs southFaceUVs = new CuboidFace.UVs(minDimension(plane.x()), minDimension(plane.y()), maxDimension(plane.x()), maxDimension(plane.y()));
        CuboidFace.UVs northFaceUVs = new CuboidFace.UVs(maxDimension(plane.x()), minDimension(plane.y()), minDimension(plane.x()), maxDimension(plane.y()));
        builder.addUnculledFace(FaceBakery.bakeQuad(interner, from, to, southFaceUVs, Quadrant.R0, materialInfo, Direction.SOUTH, modelState, null));
        builder.addUnculledFace(FaceBakery.bakeQuad(interner, from, to, northFaceUVs, Quadrant.R0, materialInfo, Direction.NORTH, modelState, null));
        bakeSideFaces(builder, interner, modelState, plane, materialInfo);
    }

    private static void bakeSideFaces(QuadCollection.Builder builder, ModelBaker.Interner interner, ModelState modelState, Vector3fc plane, BakedQuad.MaterialInfo materialInfo) {
        SpriteContents sprite = materialInfo.sprite().contents();
        // These can be 16 as the image should still be 16x16
        float xScale = 16.0F / sprite.width();
        float yScale = 16.0F / sprite.height();
        float minZ = minDimension(plane.z());
        float maxZ = maxDimension(plane.z());
        Vector3f from = new Vector3f();
        Vector3f to = new Vector3f();

        for (ExpandedItemModelGenerator.SideFace sideFace : getSideFaces(sprite)) {
            float x = sideFace.x();
            float y = sideFace.y();
            ExpandedItemModelGenerator.SideDirection sideDirection = sideFace.facing();
            float u0 = x + UV_SHRINK;
            float u1 = x + 1.0F - UV_SHRINK;
            float v0;
            float v1;
            if (sideDirection.isHorizontal()) {
                v0 = y + UV_SHRINK;
                v1 = y + 1.0F - UV_SHRINK;
            } else {
                v0 = y + 1.0F - UV_SHRINK;
                v1 = y + UV_SHRINK;
            }

            float startX = x;
            float startY = y;
            float endX = x;
            float endY = y;
            switch (sideDirection) {
                case UP:
                    endX = x + 1.0F;
                    break;
                case DOWN:
                    endX = x + 1.0F;
                    startY = y + 1.0F;
                    endY = y + 1.0F;
                    break;
                case LEFT:
                    endY = y + 1.0F;
                    break;
                case RIGHT:
                    startX = x + 1.0F;
                    endX = x + 1.0F;
                    endY = y + 1.0F;
            }

            startX *= xScale;
            endX *= xScale;
            startY *= yScale;
            endY *= yScale;
            startY = 16.0F - startY;
            endY = 16.0F - endY;
            switch (sideDirection) {
                case UP:
                    from.set(startX, startY, minZ);
                    to.set(endX, startY, maxZ);
                    break;
                case DOWN:
                    from.set(startX, endY, minZ);
                    to.set(endX, endY, maxZ);
                    break;
                case LEFT:
                    from.set(startX, startY, minZ);
                    to.set(startX, endY, maxZ);
                    break;
                case RIGHT:
                    from.set(endX, startY, minZ);
                    to.set(endX, endY, maxZ);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }

            CuboidFace.UVs uvs = new CuboidFace.UVs(u0 * xScale, v0 * yScale, u1 * xScale, v1 * yScale);
            builder.addUnculledFace(FaceBakery.bakeQuad(interner, from, to, uvs, Quadrant.R0, materialInfo, sideDirection.getDirection(), modelState, null));
        }
    }

    private static Collection<ExpandedItemModelGenerator.SideFace> getSideFaces(SpriteContents sprite) {
        int width = sprite.width();
        int height = sprite.height();
        Set<ExpandedItemModelGenerator.SideFace> sideFaces = new HashSet<>();
        sprite.getUniqueFrames().forEach(frame -> {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    boolean thisOpaque = !isTransparent(sprite, frame, x, y, width, height);
                    if (thisOpaque) {
                        checkTransition(ExpandedItemModelGenerator.SideDirection.UP, sideFaces, sprite, frame, x, y, width, height);
                        checkTransition(ExpandedItemModelGenerator.SideDirection.DOWN, sideFaces, sprite, frame, x, y, width, height);
                        checkTransition(ExpandedItemModelGenerator.SideDirection.LEFT, sideFaces, sprite, frame, x, y, width, height);
                        checkTransition(ExpandedItemModelGenerator.SideDirection.RIGHT, sideFaces, sprite, frame, x, y, width, height);
                    }
                }
            }
        });
        return sideFaces;
    }

    private static void checkTransition(
            ExpandedItemModelGenerator.SideDirection facing,
            Set<ExpandedItemModelGenerator.SideFace> sideFaces,
            SpriteContents sprite,
            int frame,
            int x,
            int y,
            int width,
            int height
    ) {
        if (isTransparent(sprite, frame, x - facing.direction.getStepX(), y - facing.direction.getStepY(), width, height)) {
            sideFaces.add(new ExpandedItemModelGenerator.SideFace(facing, x, y));
        }
    }

    private static boolean isTransparent(SpriteContents sprite, int frame, int x, int y, int width, int height) {
        return x >= 0 && y >= 0 && x < width && y < height ? sprite.isTransparent(frame, x, y) : true;
    }

    public record ItemLayerKey(Material.Baked material, ModelState modelState, Vector3fc plane, int layerIndex) implements ModelBaker.SharedOperationKey<QuadCollection> {
        @Override
        public QuadCollection compute(ModelBaker modelBakery) {
            QuadCollection.Builder builder = new QuadCollection.Builder();
            BakedQuad.MaterialInfo materialInfo = modelBakery.interner()
                    .materialInfo(BakedQuad.MaterialInfo.of(this.material, this.material.sprite().transparency(), this.layerIndex, true, 0));
            ExpandedItemModelGenerator.bakeExtrudedSprite(builder, modelBakery.interner(), this.modelState, this.plane, materialInfo);
            return builder.build();
        }
    }

    private static enum SideDirection {
        UP(Direction.UP),
        DOWN(Direction.DOWN),
        LEFT(Direction.EAST),
        RIGHT(Direction.WEST);

        private final Direction direction;

        private SideDirection(Direction direction) {
            this.direction = direction;
        }

        public Direction getDirection() {
            return this.direction;
        }

        private boolean isHorizontal() {
            return this == DOWN || this == UP;
        }
    }

    private record SideFace(ExpandedItemModelGenerator.SideDirection facing, int x, int y) { }
}
