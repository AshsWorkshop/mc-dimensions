package net.ashwork.mc.dimensions.resources;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

// NeoForge's DataMapType except for DataComponentType
public abstract class DataComponentReloadListener implements PreparableReloadListener {

    @Override
    public CompletableFuture<Void> reload(SharedState store, Executor taskExecutor, PreparationBarrier preparationBarrier, Executor reloadExecutor) {
        return null;
    }

    protected abstract HolderLookup.Provider registries(SharedState store);
}
