package net.ashwork.mc.dimensions.mixin;

import net.ashwork.mc.dimensions.extension.LootPoolExtension;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(LootPool.class)
public abstract class LootPoolMixin implements LootPoolExtension {

    @Shadow
    @Final
    private List<LootPoolEntryContainer> entries;

    @Shadow
    @Final
    private List<LootItemCondition> conditions;

    @Shadow
    @Final
    private List<LootItemFunction> functions;

    @Shadow
    public abstract NumberProvider getRolls();

    @Shadow
    public abstract NumberProvider getBonusRolls();

    @Shadow
    public abstract @Nullable String getName();

    @Override
    public LootPool.Builder ashsdimensions$mutableCopyOf() {
        var builder = LootPool.lootPool();

        builder.ashsdimensions$addAll(this.entries);
        builder.ashsdimensions$whenAll(this.conditions);
        builder.ashsdimensions$applyAll(this.functions);
        builder.setRolls(this.getRolls());
        builder.setBonusRolls(this.getBonusRolls());
        builder.name(this.getName());

        return builder;
    }
}
