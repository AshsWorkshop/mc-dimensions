package net.ashwork.mc.dimensions.mixin;

import net.ashwork.mc.dimensions.extension.AdvancementRequirementsExtension;
import net.minecraft.advancements.AdvancementRequirements;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.function.Predicate;

@Mixin(AdvancementRequirements.class)
public class AdvancementRequirementsMixin implements AdvancementRequirementsExtension {

    @Final
    @Shadow
    private List<List<String>> requirements;

    @Override
    public boolean testFlip(Predicate<String> predicate) {
        if (this.requirements.isEmpty()) {
            return false;
        } else {
            for (List<String> set : this.requirements) {
                if (set.stream().allMatch(predicate)) {
                    return true;
                }
            }

            return false;
        }
    }
}
