package net.ashwork.mc.dimensions.client.neoext;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import net.ashwork.mc.dimensions.client.neoext.item.SifterClientExtension;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;
import org.joml.Vector3f;

import java.lang.reflect.Type;
import java.util.Map;

public interface ModelExtensions {
    EnumProxy<HumanoidModel.ArmPose> SIFTER_POSE = new EnumProxy<>(
            HumanoidModel.ArmPose.class, true, true, (IArmPoseTransformer) ModelExtensions::sifterPose
    );
    ItemDisplayContext IN_SIFTER_THIRD_PERSON_LEFTHAND = IdUtils.enumExt("in_sifter_third_person_lefthand", ItemDisplayContext::valueOf);
    ItemDisplayContext IN_SIFTER_THIRD_PERSON_RIGHTHAND = IdUtils.enumExt("in_sifter_third_person_righthand", ItemDisplayContext::valueOf);
    ItemDisplayContext IN_SIFTER_FIRST_PERSON_LEFTHAND = IdUtils.enumExt("in_sifter_first_person_lefthand", ItemDisplayContext::valueOf);
    ItemDisplayContext IN_SIFTER_FIRST_PERSON_RIGHTHAND = IdUtils.enumExt("in_sifter_first_person_righthand", ItemDisplayContext::valueOf);
    Map<ItemDisplayContext, ItemTransform> TRANSFORMS = Util.make(() -> {
        ImmutableMap.Builder<ItemDisplayContext, ItemTransform> builder = ImmutableMap.builder();
        var thirdPerson = createTransform(
                new Vector3f(35f, 0f, 0f),
                new Vector3f(-5.5f, 0.25f, 1.75f),
                new Vector3f(0.375f, 0.375f, 0.375f)
        );
        builder.put(IN_SIFTER_THIRD_PERSON_LEFTHAND, thirdPerson);
        builder.put(IN_SIFTER_THIRD_PERSON_RIGHTHAND, thirdPerson);
        var firstPerson = createTransform(
                new Vector3f(0f, 0f, 0f),
                new Vector3f(-9f, 4.25f, 0f),
                new Vector3f(0.4f, 0.4f, 0.4f)
        );
        builder.put(IN_SIFTER_FIRST_PERSON_LEFTHAND, firstPerson);
        builder.put(IN_SIFTER_FIRST_PERSON_RIGHTHAND, firstPerson);
        return builder.build();
    });

    private static void sifterPose(HumanoidModel<?> model, HumanoidRenderState renderState, HumanoidArm arm) {
        model.rightArm.xRot = -2 * Mth.PI / 6;
        model.leftArm.xRot = -2 * Mth.PI / 6;
        if (renderState.isUsingItem) {
            float zRot = SifterClientExtension.animation(renderState.ticksUsingItem, 10, Mth.PI / 24);
            model.leftArm.zRot = -zRot;
            model.rightArm.zRot = -zRot;
        }
    }

    static void setup() {}

    private static ItemStack getItemInOpposite(Player player, InteractionHand hand) {
        hand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        return player.getItemInHand(hand);
    }

    /**
     * @see ItemTransform.Deserializer#deserialize(JsonElement, Type, JsonDeserializationContext) 
     */
    private static ItemTransform createTransform(Vector3f rotation, Vector3f translation, Vector3f scale) {
        translation.mul(0.0625F);
        translation.set(Mth.clamp(translation.x, -5.0F, 5.0F), Mth.clamp(translation.y, -5.0F, 5.0F), Mth.clamp(translation.z, -5.0F, 5.0F));
        scale.set(Mth.clamp(scale.x, -4.0F, 4.0F), Mth.clamp(scale.y, -4.0F, 4.0F), Mth.clamp(scale.z, -4.0F, 4.0F));
        return new ItemTransform(rotation, translation, scale);
    }
}
