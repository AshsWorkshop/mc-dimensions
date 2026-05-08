package net.ashwork.mc.dimensions.registry;

import net.ashwork.mc.dimensions.item.component.Sifting;
import net.ashwork.mc.dimensions.item.component.WoodVariant;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;

import java.util.function.Supplier;

import static net.ashwork.mc.dimensions.registry.DimensionRegistrars.*;

public interface DimensionDataComponents {

    Supplier<DataComponentType<WoodVariant>> WOOD_VARIANT = DATA_COMPONENT.registerComponentType(
            "wood_varant", builder -> builder.persistent(WoodVariant.CODEC)
    );
    Supplier<DataComponentType<Sifting>> SIFTING = DATA_COMPONENT.registerComponentType(
            "sifting", builder -> builder.persistent(Sifting.CODEC).networkSynchronized(Sifting.STREAM_CODEC)
    );

    static void register() {}
}
