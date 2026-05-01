package net.ashwork.mc.dimensions.data.server;

import net.ashwork.mc.dimensions.registry.DimensionItems;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DimensionsRecipeProvider extends RecipeProvider {

    protected DimensionsRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        this.speckToNugget(Items.GOLD_NUGGET, DimensionItems.GOLD_SPECK);
        this.speckToNugget(Items.IRON_NUGGET, DimensionItems.IRON_SPECK);
        this.speckToNugget(Items.COPPER_NUGGET, DimensionItems.COPPER_SPECK);
    }

    private void speckToNugget(ItemLike nugget, Holder<? extends ItemLike> speck) {
        this.twoByTwoPacker(RecipeCategory.MISC, nugget, speck.value(), "from_speck");
    }

    private void twoByTwoPacker(RecipeCategory category, ItemLike result, ItemLike ingredient, @Nullable String suffix) {
        this.shaped(category, result, 1)
                .define('#', ingredient)
                .pattern("##").pattern("##")
                .unlockedBy(getHasName(ingredient), this.has(ingredient))
                .save(this.output, IdUtils.recipe(getItemName(ingredient), suffix));
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
