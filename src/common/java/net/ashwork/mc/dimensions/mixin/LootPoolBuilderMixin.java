package net.ashwork.mc.dimensions.mixin;

import com.google.common.collect.ImmutableList;
import net.ashwork.mc.dimensions.extension.LootPoolBuilderExtension;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(LootPool.Builder.class)
public class LootPoolBuilderMixin implements LootPoolBuilderExtension {

    @Shadow
    @Final
    private ImmutableList.Builder<LootPoolEntryContainer> entries;

    @Shadow
    @Final
    private ImmutableList.Builder<LootItemCondition> conditions;

    @Shadow
    @Final
    private ImmutableList.Builder<LootItemFunction> functions;

    @Override
    public LootPool.Builder ashsdimensions$addAll(List<LootPoolEntryContainer> entries) {
        this.entries.addAll(entries);
        return (LootPool.Builder) (Object) this;
    }

    @Override
    public LootPool.Builder ashsdimensions$whenAll(List<LootItemCondition> conditions) {
        this.conditions.addAll(conditions);
        return (LootPool.Builder) (Object) this;
    }

    @Override
    public LootPool.Builder ashsdimensions$applyAll(List<LootItemFunction> functions) {
        this.functions.addAll(functions);
        return (LootPool.Builder) (Object) this;
    }
}
