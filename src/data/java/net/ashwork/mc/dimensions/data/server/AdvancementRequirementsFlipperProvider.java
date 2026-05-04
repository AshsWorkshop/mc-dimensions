package net.ashwork.mc.dimensions.data.server;

import com.mojang.serialization.Codec;
import net.ashwork.mc.dimensions.AshsDimensions;
import net.ashwork.mc.dimensions.registry.DimensionItems;
import net.ashwork.mc.dimensions.resources.AdvancementRequirementsFlipper;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.concurrent.CompletableFuture;

public class AdvancementRequirementsFlipperProvider extends JsonCodecProvider<AdvancementRequirementsFlipper.Entry> {

    public AdvancementRequirementsFlipperProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, PackOutput.Target.DATA_PACK, AdvancementRequirementsFlipper.PATH, AdvancementRequirementsFlipper.Entry.CODEC, lookupProvider, AshsDimensions.ID);
    }

    @Override
    protected void gather() {
        this.unconditional(IdUtils.id("sifters"), new AdvancementRequirementsFlipper.Entry(
                DimensionItems.SIFTERS.values().stream().map(
                        sifter -> sifter.getKey().identifier().withPrefix("recipes/" + RecipeCategory.TOOLS.getFolderName() + "/")
                ).toList()
        ));
    }
}
