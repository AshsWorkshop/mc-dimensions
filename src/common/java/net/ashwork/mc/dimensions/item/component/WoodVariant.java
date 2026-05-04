package net.ashwork.mc.dimensions.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.function.Consumer;

public record WoodVariant(WoodType type, Component label) implements TooltipProvider {

    public static final Codec<WoodVariant> CODEC = WoodType.CODEC.xmap(WoodVariant::new, WoodVariant::type);

    public static final String VARIANT_LABEL = Util.makeDescriptionId(
            "label", Identifier.fromNamespaceAndPath("wood_type", "title")
    );

    public static String descriptionId(WoodType type) {
        return Util.makeDescriptionId("wood_type", Identifier.parse(type.name()));
    }

    public WoodVariant(WoodType type) {
        this(type, Component.translatable(descriptionId(type)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        if (flag.isAdvanced() || flag.hasShiftDown()) {
            consumer.accept(Component.translatable(VARIANT_LABEL, this.label).withStyle(ChatFormatting.GOLD));
        }
    }
}
