package net.ashwork.mc.dimensions.resources;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ashwork.mc.dimensions.network.ClientboundFlipAdvancementRequirementsDataPayload;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdvancementRequirementsFlipper extends SimpleJsonResourceReloadListener<AdvancementRequirementsFlipper.Entry> {

    public static final Identifier ID = IdUtils.id("flip_advancement_requirements");
    public static final String PATH = IdUtils.idPath("flip_advancement_requirements");
    public static final AdvancementRequirementsFlipper INSTANCE = new AdvancementRequirementsFlipper();
    private final Set<Identifier> advancements;

    private AdvancementRequirementsFlipper() {
        super(Entry.CODEC, FileToIdConverter.json(PATH));
        this.advancements = new HashSet<>();
    }

    @Override
    protected void apply(Map<Identifier, Entry> preparations, ResourceManager manager, ProfilerFiller profiler) {
        this.advancements.clear();

        preparations.values().forEach(entry -> {
            if (entry.replace()) this.advancements.clear();

            this.advancements.addAll(entry.values());
        });
    }

    public boolean shouldFlipRequirements(@Nullable Identifier id) {
        return id == null ? false : this.advancements.contains(id);
    }

    public ClientboundFlipAdvancementRequirementsDataPayload createPayload() {
        return new ClientboundFlipAdvancementRequirementsDataPayload(this.advancements.stream().toList());
    }

    public void setRequirements(Collection<Identifier> values) {
        this.advancements.clear();
        this.advancements.addAll(values);
    }

    public record Entry(List<Identifier> values, boolean replace) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.listOf().fieldOf("values").forGetter(Entry::values),
                Codec.BOOL.optionalFieldOf("replace", false).forGetter(Entry::replace)
        ).apply(instance, Entry::new));

        public Entry(List<Identifier> values) {
            this(values, false);
        }
    }
}
