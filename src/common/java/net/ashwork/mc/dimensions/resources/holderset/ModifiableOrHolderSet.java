package net.ashwork.mc.dimensions.resources.holderset;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.registries.holdersets.HolderSetType;
import net.neoforged.neoforge.registries.holdersets.ICustomHolderSet;
import net.neoforged.neoforge.registries.holdersets.OrHolderSet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModifiableOrHolderSet<T> extends LazyDelegateHolderSet<T> {

    private final HolderSet<T> original;
    private final List<HolderSet<T>> sets;

    public ModifiableOrHolderSet(HolderSet<T> original) {
        this.original = original;
        this.sets = new ArrayList<>();
        this.sets.add(this.original);
    }

    public void add(HolderSet<T> set) {
        if (this.isResolved()) {
            throw new IllegalStateException("Cannot add elements to a resolved holder set!");
        }
        this.sets.add(set);
    }

    @Override
    protected Supplier<ICustomHolderSet<T>> delegateSupplier() {
        return () -> new OrHolderSet<>(this.sets);
    }

    @Override
    public HolderSet<T> toSet() {
        return this.sets.size() > 1 ? this.resolve() : this.original;
    }
}
