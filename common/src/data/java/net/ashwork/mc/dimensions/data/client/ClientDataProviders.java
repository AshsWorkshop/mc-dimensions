package net.ashwork.mc.dimensions.data.client;

import net.ashwork.mc.dimensions.data.DimensionsData;
import net.ashwork.mc.dimensions.data.server.DimensionsRecipeProvider;
import net.ashwork.mc.dimensions.registry.DimensionBlocksWithItems;
import net.ashwork.mc.dimensions.registry.DimensionItems;
import net.ashwork.mc.multiloader.api.common.event.resources.RegisterBuiltInPacks;
import net.ashwork.mc.multiloader.api.data.generator.DataProviderGatherer;
import net.ashwork.mc.multiloader.api.data.generator.provider.client.ModelCreator;
import net.ashwork.mc.multiloader.api.data.generator.provider.client.TranslationKeyMapper;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public interface ClientDataProviders {

    static void gather(DataProviderGatherer gatherer) {
        gatherer.access(TranslationKeyMapper.ENGLISH_PROVIDER).add(ClientDataProviders::englishTranslations);
        gatherer.access(ModelCreator.FLAT_PROVIDER).add(ClientDataProviders::blockModels, ClientDataProviders::itemModels);
    }

    static void englishTranslations(TranslationKeyMapper mapper) {
        var alt = DimensionsData.PLATFORM.withId("alternative");
        mapper.descriptionId(RegisterBuiltInPacks.RESOURCE_PACK_ID, alt, "Dimensions | Alternatives");
        mapper.descriptionId(RegisterBuiltInPacks.RESOURCE_PACK_ID, RegisterBuiltInPacks.RESOURCE_PACK_DESC, alt, "Alternative configurations for Ash's Dimensions");

        mapper.item(DimensionItems.GOLD_SPECK, "Gold Speck");
        mapper.item(DimensionItems.IRON_SPECK, "Iron Speck");
        mapper.item(DimensionItems.COPPER_SPECK, "Copper Speck");

        mapper.block(DimensionBlocksWithItems.SIFTED_SAND, "Sand (Sifted)");
    }

    static void itemModels(ItemModelGenerators generators) {
        generators.generateFlatItem(DimensionItems.GOLD_SPECK.value(), ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(DimensionItems.IRON_SPECK.value(), ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(DimensionItems.COPPER_SPECK.value(), ModelTemplates.FLAT_ITEM);
    }

    static void blockModels(BlockModelGenerators generators) {
        generators.createRotatedVariantBlock(DimensionBlocksWithItems.SIFTED_SAND.value());
    }
}
