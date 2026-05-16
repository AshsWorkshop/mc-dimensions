package net.ashwork.mc.dimensions.data.server;

import com.google.common.collect.Sets;
import net.ashwork.mc.dimensions.registry.DimensionItems;
import net.ashwork.mc.dimensions.resources.AdvancementRequirementsFlipper;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.criterion.ImpossibleTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
        this.shardToItem(Items.DIAMOND, DimensionItems.DIAMOND_SHARD);
        DimensionItems.SIFTERS.forEach((wood, sifter) ->
                this.sifter(RecipeCategory.TOOLS, wood, Items.STRING, sifter));
    }


    private void sifter(RecipeCategory category, WoodType wood, ItemLike weave, Holder<? extends ItemLike> sifterHolder) {
        ItemLike sifter = sifterHolder.value();
        ItemLike planks = this.items.getOrThrow(ResourceKey.create(Registries.ITEM, Identifier.parse(wood.name()).withSuffix("_planks"))).value();
        this.shaped(category, sifter, 1)
                .define('P', planks)
                .define('S', weave)
                .pattern("P P").pattern("PSP")
                .group(IdUtils.idString("sifter"))
                .unlockedBy(getHasName(sifter), this.has(sifter))
                .unlockedBy("has_any_sifter", this.has(DimensionItemTags.SIFTERS))
                .unlockedBy(getHasName(planks), this.has(planks))
                .ashsdimensions$requirements(new AdvancementRequirements(List.of(
                        List.of("has_the_recipe"),
                        List.of(getHasName(sifter)),
                        List.of(
                                "has_any_sifter",
                                getHasName(planks)
                        )
                )))
                .ashsdimensions$useOROfANDs(IdUtils.id("sifters"))
                .save(this.output);
    }

    private void speckToNugget(ItemLike nugget, Holder<? extends ItemLike> speck) {
        this.twoByTwoPacker(RecipeCategory.MISC, nugget, speck.value(), "from_speck");
    }

    private void shardToItem(ItemLike result, Holder<? extends ItemLike> shard) {
        this.twoByTwoPacker(RecipeCategory.MISC, result, shard.value(), "from_shard");
    }

    private void twoByTwoPacker(RecipeCategory category, ItemLike result, ItemLike ingredient, @Nullable String suffix) {
        this.shaped(category, result, 1)
                .define('#', ingredient)
                .pattern("##").pattern("##")
                .unlockedBy(getHasName(ingredient), this.has(ingredient))
                .save(this.output, IdUtils.recipe(getItemName(ingredient), suffix));
    }

    public interface RecipeOutputExtension extends RecipeOutput {
        void acceptFlipper(Identifier id, @Nullable Identifier group);
    }

    public static class RunnerWrapper implements DataProvider {
        private final PackOutput packOutput;
        private final CompletableFuture<HolderLookup.Provider> registries;

        public RunnerWrapper(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            this.packOutput = packOutput;
            this.registries = registries;
        }

        @Override
        public final CompletableFuture<?> run(CachedOutput cache) {
            return this.registries
                    .thenCompose(
                            registries -> {
                                final PackOutput.PathProvider recipePathProvider = this.packOutput.createRegistryElementsPathProvider(Registries.RECIPE);
                                final PackOutput.PathProvider advancementPathProvider = this.packOutput.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
                                final PackOutput.PathProvider flipRequirementsProvider = this.packOutput.createPathProvider(PackOutput.Target.DATA_PACK, AdvancementRequirementsFlipper.PATH);
                                final Set<ResourceKey<Recipe<?>>> allRecipes = Sets.newHashSet();
                                final Map<Identifier, List<Identifier>> flippers = new HashMap<>();
                                final List<CompletableFuture<?>> tasks = new ArrayList<>();
                                RecipeOutput recipeOutput = new RecipeOutputExtension() {
                                    @Override
                                    public void acceptFlipper(Identifier id, @Nullable Identifier group) {
                                        if (group == null) group = IdUtils.id("recipes");
                                        flippers.computeIfAbsent(group, g -> new ArrayList<>()).add(id);
                                    }

                                    @Override
                                    public void accept(ResourceKey<Recipe<?>> id, Recipe<?> recipe, @Nullable AdvancementHolder advancementHolder, net.neoforged.neoforge.common.conditions.ICondition... conditions) {
                                        if (!allRecipes.add(id)) {
                                            throw new IllegalStateException("Duplicate recipe " + id.identifier());
                                        } else {
                                            this.saveRecipe(id, recipe, conditions);
                                            if (advancementHolder != null) {
                                                this.saveAdvancement(advancementHolder, conditions);
                                            }
                                        }
                                    }

                                    @Override
                                    public Advancement.Builder advancement() {
                                        return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
                                    }

                                    @Override
                                    public void includeRootAdvancement() {
                                        AdvancementHolder root = Advancement.Builder.recipeAdvancement()
                                                .addCriterion("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                                                .build(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
                                        this.saveAdvancement(root);
                                    }

                                    private void saveRecipe(ResourceKey<Recipe<?>> id, Recipe<?> recipe) {
                                        saveRecipe(id, recipe, new net.neoforged.neoforge.common.conditions.ICondition[0]);
                                    }

                                    private void saveRecipe(ResourceKey<Recipe<?>> id, Recipe<?> recipe, net.neoforged.neoforge.common.conditions.ICondition... conditions) {
                                        tasks.add(DataProvider.saveStable(cache, registries, Recipe.CONDITIONAL_CODEC, Optional.of(new net.neoforged.neoforge.common.conditions.WithConditions<>(recipe, conditions)), recipePathProvider.json(id.identifier())));
                                    }

                                    private void saveAdvancement(AdvancementHolder advancementHolder) {
                                        saveAdvancement(advancementHolder, new net.neoforged.neoforge.common.conditions.ICondition[0]);
                                    }

                                    private void saveAdvancement(AdvancementHolder advancementHolder, net.neoforged.neoforge.common.conditions.ICondition... conditions) {
                                        tasks.add(
                                                DataProvider.saveStable(
                                                        cache, registries, Advancement.CONDITIONAL_CODEC, Optional.of(new net.neoforged.neoforge.common.conditions.WithConditions<>(advancementHolder.value(), conditions)), advancementPathProvider.json(advancementHolder.id())
                                                )
                                        );
                                    }
                                };
                                new DimensionsRecipeProvider(registries, recipeOutput).buildRecipes();
                                flippers.forEach((id, values) -> tasks.add(
                                        DataProvider.saveStable(
                                                cache, registries, AdvancementRequirementsFlipper.Entry.CODEC,
                                                new AdvancementRequirementsFlipper.Entry(values), flipRequirementsProvider.json(id)
                                        )
                                ));
                                return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
                            }
                    );
        }

        @Override
        public String getName() {
            return "recipes";
        }
    }
}
