package net.ashwork.mc.dimensions.item;

import net.ashwork.mc.dimensions.item.component.Sifting;
import net.ashwork.mc.dimensions.item.component.WoodVariant;
import net.ashwork.mc.dimensions.neoext.ItemExtensions;
import net.ashwork.mc.dimensions.registry.DimensionDataComponents;
import net.ashwork.mc.dimensions.registry.DimensionDepositables;
import net.ashwork.mc.dimensions.registry.DimensionSounds;
import net.ashwork.mc.dimensions.storage.siftable.Siftable;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class SifterItem extends Item {

    public SifterItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        itemStack.addToTooltip(DimensionDataComponents.WOOD_VARIANT, context, display, builder, tooltipFlag);
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        var siftingItemHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        var siftingItem = player.getItemInHand(siftingItemHand);
        var siftable = siftingItem.typeHolder().getData(Siftable.DATA_MAP);
        if (siftable != null) {
            var lootKey = siftable.use(siftingItem);
            if (lootKey.isPresent()) {
                var sifter = player.getItemInHand(hand);
                sifter.set(DimensionDataComponents.SIFTING, Sifting.create(siftingItem, siftingItemHand, lootKey.get(), siftable.sound()));
                player.startUsingItem(hand);
                return InteractionResult.CONSUME.heldItemTransformedTo(sifter);
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
        return ItemExtensions.USE_SIFTER;
    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        var sifting = itemStack.get(DimensionDataComponents.SIFTING);
        return sifting != null ? sifting.tickDuration() : super.getUseDuration(itemStack, user);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
        if (ticksRemaining >= 0) {
            var sifting = itemStack.get(DimensionDataComponents.SIFTING);
            if (sifting == null || !sifting.canKeepUsing(livingEntity)) {
                livingEntity.releaseUsingItem();
            } else if ((ticksRemaining + 1) % 10 == 0 && sifting.sound().isPresent()) {
                level.playSound(livingEntity, livingEntity.blockPosition(), sifting.sound().get().value(), SoundSource.BLOCKS);
            }
        } else {
            livingEntity.releaseUsingItem();
        }
    }

    @Override
    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
        itemStack.remove(DimensionDataComponents.SIFTING);
        return remainingTime <= 0 ? true : super.releaseUsing(itemStack, level, entity, remainingTime);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        var sifting = itemStack.get(DimensionDataComponents.SIFTING);
        if (sifting != null && level instanceof ServerLevel serverLevel) {
            sifting.finishUsingItem(itemStack, serverLevel, entity);
            itemStack.hurtAndBreak(1, entity, sifting.slot());
        }
        itemStack.remove(DimensionDataComponents.SIFTING);
        return super.finishUsingItem(itemStack, level, entity);
    }
}
