package net.ashwork.mc.dimensions.data.client;

import net.ashwork.mc.dimensions.AshsDimensions;
import net.ashwork.mc.dimensions.registry.DimensionSounds;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class DimensionsSoundDefinitionsProvider extends SoundDefinitionsProvider {

    public DimensionsSoundDefinitionsProvider(PackOutput output) {
        super(output, AshsDimensions.ID);
    }

    @Override
    public void registerSounds() {
        this.add(DimensionSounds.SIFTING_SAND, SoundDefinition.definition()
                .with(SoundDefinition.Sound.sound(IdUtils.id("sifting/sand"), SoundDefinition.SoundType.SOUND)));
    }
}
