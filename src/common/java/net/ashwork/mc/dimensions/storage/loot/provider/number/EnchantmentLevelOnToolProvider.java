package net.ashwork.mc.dimensions.storage.loot.provider.number;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jspecify.annotations.Nullable;

import java.util.Set;

public record EnchantmentLevelOnToolProvider(Holder<Enchantment> enchantment) implements NumberProvider {

    public static final MapCodec<EnchantmentLevelOnToolProvider> MAP_CODEC = Enchantment.CODEC.xmap(
            EnchantmentLevelOnToolProvider::new, EnchantmentLevelOnToolProvider::enchantment
    ).fieldOf("enchantment");

    @Override
    public float getFloat(LootContext context) {
        @Nullable ItemInstance tool = context.getParameter(LootContextParams.TOOL);
        return EnchantmentHelper.getTagEnchantmentLevel(this.enchantment, tool);
    }

    @Override
    public Set<ContextKey<?>> getReferencedContextParams() {
        return Set.of(LootContextParams.TOOL);
    }

    @Override
    public MapCodec<? extends NumberProvider> codec() {
        return MAP_CODEC;
    }
}
