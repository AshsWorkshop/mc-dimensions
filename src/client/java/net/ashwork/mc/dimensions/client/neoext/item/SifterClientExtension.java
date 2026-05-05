package net.ashwork.mc.dimensions.client.neoext.item;

import net.ashwork.mc.dimensions.client.neoext.ModelExtensions;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jspecify.annotations.Nullable;

public record SifterClientExtension() implements IClientItemExtensions {

    public static final SifterClientExtension INSTANCE = new SifterClientExtension();

    @Override
    public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
        return itemStack.is(DimensionItemTags.SIFTERS) ? ModelExtensions.SIFTER_POSE.getValue()
                : IClientItemExtensions.super.getArmPose(entityLiving, hand, itemStack);
    }
}
