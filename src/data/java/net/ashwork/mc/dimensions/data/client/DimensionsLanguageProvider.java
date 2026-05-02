package net.ashwork.mc.dimensions.data.client;

import net.ashwork.mc.dimensions.AshsDimensions;
import net.ashwork.mc.dimensions.registry.DimensionBlocks;
import net.ashwork.mc.dimensions.registry.DimensionItems;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class DimensionsLanguageProvider extends LanguageProvider {

    public DimensionsLanguageProvider(PackOutput output) {
        super(output, AshsDimensions.ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.item(DimensionItems.GOLD_SPECK, "Gold Speck");
        this.item(DimensionItems.IRON_SPECK, "Iron Speck");
        this.item(DimensionItems.COPPER_SPECK, "Copper Speck");

        this.block(DimensionBlocks.SIFTED_SAND, "Sand (Sifted)");
    }

    private void item(Holder<? extends Item> item, String name) {
        this.add(item.value(), name);
    }

    private void block(Holder<? extends Block> block, String name) {
        this.add(block.value(), name);
    }
}
