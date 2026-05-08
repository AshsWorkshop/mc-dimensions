package net.ashwork.mc.dimensions.neoext;

import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.world.item.ItemUseAnimation;

public interface ItemExtensions {
    ItemUseAnimation USE_SIFTER = IdUtils.enumExt("sifter", ItemUseAnimation::valueOf);
}
