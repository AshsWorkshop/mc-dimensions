package net.ashwork.mc.dimensions.storage.loot.modifier;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public abstract class AbstractLootTableModifier implements LootTableModifier {

    protected final int priority;

    protected static <T extends AbstractLootTableModifier> Products.P1<RecordCodecBuilder.Mu<T>, Integer> codecStart(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(
                Codec.INT.optionalFieldOf("priority", LootTableModifier.DEFAULT_PRIORITY).forGetter(LootTableModifier::priority)
        );
    }

    protected AbstractLootTableModifier(int priority) {
        this.priority = priority;
    }

    @Override
    public int priority() {
        return this.priority;
    }
}
