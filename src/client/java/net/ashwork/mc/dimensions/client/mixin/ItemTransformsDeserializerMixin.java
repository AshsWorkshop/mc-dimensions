package net.ashwork.mc.dimensions.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.ashwork.mc.dimensions.client.neoext.ModelExtensions;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ItemTransforms.Deserializer.class)
public class ItemTransformsDeserializerMixin {

    @ModifyExpressionValue(
            method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/resources/model/cuboid/ItemTransforms;",
            at = @At(
                    value = "INVOKE", target = "Lnet/minecraft/client/resources/model/cuboid/ItemTransforms$Deserializer;getTransform(Lcom/google/gson/JsonDeserializationContext;Lcom/google/gson/JsonObject;Lnet/minecraft/world/item/ItemDisplayContext;)Lnet/minecraft/client/resources/model/cuboid/ItemTransform;",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;builder()Lcom/google/common/collect/ImmutableMap$Builder;")
            )
    )
    private ItemTransform addDefaultTransform(ItemTransform original, @Local(name = "type") ItemDisplayContext type) {
        if (original == ItemTransform.NO_TRANSFORM) {
            var transform = ModelExtensions.TRANSFORMS.get(type);
            if (transform != null) {
                return transform;
            }
        }
        return original;
    }
}
