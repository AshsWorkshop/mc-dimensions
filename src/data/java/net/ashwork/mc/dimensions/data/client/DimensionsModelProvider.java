package net.ashwork.mc.dimensions.data.client;

import net.ashwork.mc.dimensions.AshsDimensions;
import net.ashwork.mc.dimensions.client.neoext.ModelExtensions;
import net.ashwork.mc.dimensions.data.client.model.ExpandedItemModelGeneratorBuilder;
import net.ashwork.mc.dimensions.registry.DimensionBlocks;
import net.ashwork.mc.dimensions.registry.DimensionItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.List;
import java.util.Set;

import static net.ashwork.mc.dimensions.util.IdUtils.idString;

public class DimensionsModelProvider extends ModelProvider {

    public DimensionsModelProvider(PackOutput output) {
        super(output, AshsDimensions.ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        speck(itemModels, DimensionItems.GOLD_SPECK);
        speck(itemModels, DimensionItems.IRON_SPECK);
        speck(itemModels, DimensionItems.COPPER_SPECK);
        DimensionItems.SIFTERS.forEach((wood, sifter) -> sifter(itemModels, wood, sifter));

        blockModels.createRotatedVariantBlock(DimensionBlocks.SIFTED_SAND.value());
    }

    private static void sifter(ItemModelGenerators itemModels, WoodType wood, Holder<? extends Item> sifter) {
        var model = Templates.SIFTER.create(
                sifter.value(), new TextureMapping()
                        .put(Slots.BASKET, TextureMapping.getBlockTexture(BuiltInRegistries.BLOCK.getValue(
                                Identifier.parse(wood.name()).withSuffix("_planks")
                        )))
                        .put(Slots.WEAVE, new Material(ModelLocationUtils.decorateBlockModelLocation(idString("sifter_weave")))),
                itemModels.modelOutput
        );
        var itemModel = ItemModelUtils.plainModel(model);
        itemModels.itemModelOutput.accept(
                sifter.value(), ItemModelUtils.select(
                        new DisplayContext(), ItemModelUtils.plainModel(
                                ModelTemplates.FLAT_ITEM.create(
                                        ModelLocationUtils.getModelLocation(sifter.value(), "_flat"),
                                        TextureMapping.layer0(sifter.value()),
                                        itemModels.modelOutput
                                )
                        ), ItemModelUtils.when(
                                List.of(
                                        ModelExtensions.IN_SIFTER_FIRST_PERSON_LEFTHAND,
                                        ModelExtensions.IN_SIFTER_FIRST_PERSON_RIGHTHAND,
                                        ModelExtensions.IN_SIFTER_THIRD_PERSON_LEFTHAND,
                                        ModelExtensions.IN_SIFTER_THIRD_PERSON_RIGHTHAND
                                ),
                                itemModel
                        )
                )
        );
    }

    private static void speck(ItemModelGenerators itemModels, Holder<? extends Item> speck) {
        itemModels.generateFlatItem(speck.value(), ExtendedModelTemplateBuilder.builder()
                .requiredTextureSlot(TextureSlot.LAYER0)
                .customLoader(ExpandedItemModelGeneratorBuilder::new, loader -> loader.size(4f))
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, builder -> builder.translation(0f, 0.75f, 1f).scale(0.55f, 0.55f, 0.55f))
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, builder -> builder.translation(0f, 0.75f, 1f).scale(0.55f, 0.55f, 0.55f))
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, builder -> builder.rotation(0f, -90f, 25f).translation(1.13f, 3.2f, 3.25f).scale(0.68f, 0.68f, 0.68f))
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, builder -> builder.rotation(0f, -90f, 25f).translation(1.13f, 3.2f, 3.25f).scale(0.68f, 0.68f, 0.68f))
                .transform(ItemDisplayContext.GROUND, builder -> builder.translation(0f, 3f, 0f).scale(0.5f, 0.5f, 0.5f))
                .transform(ItemDisplayContext.HEAD, builder -> builder.rotation(0f, -180f, 0f).translation(0f, 13f, 7f))
                .transform(ItemDisplayContext.FIXED, builder -> builder.rotation(0f, -180f, 0f))
                .transform(ItemDisplayContext.ON_SHELF, builder -> builder.rotation(0f, -180f, 0f))
                .build()
        );
    }

    public interface Slots {
        TextureSlot BASKET = TextureSlot.create("basket");
        TextureSlot WEAVE = TextureSlot.create("weave");
    }

    public interface Templates {
        ModelTemplate SIFTER = ModelTemplates.createItem(
                idString("template_sifter"), Slots.BASKET, Slots.WEAVE
        );
    }
}
