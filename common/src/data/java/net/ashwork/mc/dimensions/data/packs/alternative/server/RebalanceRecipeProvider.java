package net.ashwork.mc.dimensions.data.packs.alternative.server;

import net.ashwork.mc.dimensions.Dimensions;
import net.ashwork.mc.dimensions.registry.DimensionItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class RebalanceRecipeProvider extends RecipeProvider {

    protected RebalanceRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        this.threeBythreePacker(RecipeCategory.MISC, Items.GOLD_NUGGET, DimensionItems.GOLD_SPECK.value(), "from_speck");
        this.threeBythreePacker(RecipeCategory.MISC, Items.IRON_NUGGET, DimensionItems.IRON_SPECK.value(), "from_speck");
        this.threeBythreePacker(RecipeCategory.MISC, Items.COPPER_NUGGET, DimensionItems.COPPER_SPECK.value(), "from_speck");
    }

    protected void threeBythreePacker(RecipeCategory category, ItemLike result, ItemLike ingredient, String suffix) {
        this.shaped(category, result, 1)
                .define('#', ingredient)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(ingredient), this.has(ingredient))
                .save(this.output, Dimensions.PLATFORM.withId(getItemName(result) + "_" + suffix).toString());
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new RebalanceRecipeProvider(provider, recipeOutput);
        }

        @Override
        public String getName() {
            return "recipes";
        }
    }
}
