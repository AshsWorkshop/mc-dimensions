package net.ashwork.mc.dimensions.registry;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import static net.ashwork.mc.dimensions.registry.DimensionRegistrars.*;

public interface DimensionSounds {

    Holder<SoundEvent> SIFTING_SAND = registerSound("sifting.sand");

    private static Holder<SoundEvent> registerSound(String name) {
        return SOUND_EVENT.register(name, key -> SoundEvent.createVariableRangeEvent(key));
    }

    static void register() {}
}
