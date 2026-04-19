package net.ashwork.mc.dimensions.data.server;

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

public class DimensionsRecipeProvider extends RecipeProvider {

    protected DimensionsRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        this.twoByTwoPacker(RecipeCategory.MISC, Items.GOLD_NUGGET, DimensionItems.GOLD_SPECK.value(), "from_speck");
    }

    protected void twoByTwoPacker(RecipeCategory category, ItemLike result, ItemLike ingredient, String suffix) {
        this.shaped(category, result, 1)
                .define('#', ingredient)
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(ingredient), this.has(ingredient))
                .save(this.output, Dimensions.PLATFORM.withId(getItemName(result) + "_" + suffix).toString());
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new DimensionsRecipeProvider(provider, recipeOutput);
        }

        @Override
        public String getName() {
            return "recipes";
        }
    }
}
