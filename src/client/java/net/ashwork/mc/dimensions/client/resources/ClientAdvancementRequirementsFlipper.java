package net.ashwork.mc.dimensions.client.resources;

import net.ashwork.mc.dimensions.resources.advancement.AdvancementRequirementsFlipper;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ClientAdvancementRequirementsFlipper implements AdvancementRequirementsFlipper {

    public static final ClientAdvancementRequirementsFlipper INSTANCE = new ClientAdvancementRequirementsFlipper();
    private final Set<Identifier> advancements;
    private ClientAdvancementRequirementsFlipper() {
        this.advancements = new HashSet<>();
    }

    public void setRequirements(Collection<Identifier> values) {
        this.advancements.clear();
        this.advancements.addAll(values);
    }

    @Override
    public boolean shouldFlipRequirements(@Nullable Identifier advancementId) {
        return advancementId == null ? false : this.advancements.contains(advancementId);
    }
}
