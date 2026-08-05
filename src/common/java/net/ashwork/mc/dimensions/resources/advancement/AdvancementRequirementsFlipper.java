package net.ashwork.mc.dimensions.resources.advancement;

import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public interface AdvancementRequirementsFlipper {

    boolean shouldFlipRequirements(@Nullable Identifier advancementId);
}
