package net.ashwork.mc.dimensions.resources.holderset;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.registries.holdersets.HolderSetType;
import net.neoforged.neoforge.registries.holdersets.ICustomHolderSet;
import org.jspecify.annotations.NonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class LazyDelegateHolderSet<T> implements ICustomHolderSet<T> {

    private ICustomHolderSet<T> delegate;

    protected LazyDelegateHolderSet() {}

    protected abstract Supplier<ICustomHolderSet<T>> delegateSupplier();

    protected boolean isResolved() {
        return this.delegate != null;
    }

    public abstract HolderSet<T> toSet();

    protected ICustomHolderSet<T> resolve() {
        if (this.delegate == null) {
            synchronized (this) {
                this.delegate = this.delegateSupplier().get();
            }
        }
        return this.delegate;
    }

    @Override
    public HolderSetType type() {
        return this.resolve().type();
    }

    @Override
    public SerializationType serializationType() {
        return this.resolve().serializationType();
    }

    @Override
    public Stream<Holder<T>> stream() {
        return this.resolve().stream();
    }

    @Override
    public int size() {
        return this.resolve().size();
    }

    @Override
    public boolean isBound() {
        return this.resolve().isBound();
    }

    @Override
    public Either<TagKey<T>, List<Holder<T>>> unwrap() {
        return this.resolve().unwrap();
    }

    @Override
    public Optional<Holder<T>> getRandomElement(RandomSource random) {
        return this.resolve().getRandomElement(random);
    }

    @Override
    public Holder<T> get(int index) {
        return this.resolve().get(index);
    }

    @Override
    public boolean contains(Holder<T> value) {
        return this.resolve().contains(value);
    }

    @Override
    public boolean canSerializeIn(HolderOwner<T> owner) {
        return this.resolve().canSerializeIn(owner);
    }

    @Override
    public Optional<TagKey<T>> unwrapKey() {
        return this.resolve().unwrapKey();
    }

    @Override
    public @NonNull Iterator<Holder<T>> iterator() {
        return this.resolve().iterator();
    }

    @Override
    public void addInvalidationListener(Runnable runnable) {
        this.resolve().addInvalidationListener(runnable);
    }

    @Override
    public boolean isImmediatelyResolvable() {
        return this.resolve().isImmediatelyResolvable();
    }

    @Override
    public String toString() {
        return this.resolve().toString();
    }
}
