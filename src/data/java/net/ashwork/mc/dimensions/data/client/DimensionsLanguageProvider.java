package net.ashwork.mc.dimensions.data.client;

import net.ashwork.mc.dimensions.AshsDimensions;import net.ashwork.mc.dimensions.item.component.WoodVariant;
import net.ashwork.mc.dimensions.registry.DimensionBlocks;
import net.ashwork.mc.dimensions.registry.DimensionItems;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class DimensionsLanguageProvider extends LanguageProvider {

    public DimensionsLanguageProvider(PackOutput output) {
        super(output, AshsDimensions.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.item(DimensionItems.GOLD_SPECK, "Gold Speck");
        this.item(DimensionItems.IRON_SPECK, "Iron Speck");
        this.item(DimensionItems.COPPER_SPECK, "Copper Speck");
        this.item(DimensionItems.DIAMOND_SHARD, "Diamond Shard");

        this.block(DimensionBlocks.SIFTED_SAND, "Sand (Sifted)");

        this.add(WoodVariant.VARIANT_LABEL, "Wood: %1$s");
        DimensionItems.SIFTERS.forEach((type, sifter) -> {
            this.add(
                    WoodVariant.descriptionId(type), Arrays.stream(type.name().split("_"))
                            .map(name -> name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1))
                            .collect(Collectors.joining(" "))
            );
            this.item(sifter, "Sifter");
        });
    }

    private void item(Holder<? extends Item> item, String name) {
        this.add(item.value(), name);
    }

    private void block(Holder<? extends Block> block, String name) {
        this.add(block.value(), name);
    }
}
