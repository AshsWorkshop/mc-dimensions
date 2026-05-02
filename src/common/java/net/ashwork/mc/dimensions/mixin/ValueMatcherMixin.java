package net.ashwork.mc.dimensions.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.ashwork.mc.dimensions.storage.matcher.DimensionValueMatcher;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Mixin(StatePropertiesPredicate.ValueMatcher.class)
public interface ValueMatcherMixin {


    @Definition(id = "CODEC", field = "Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$ValueMatcher;CODEC:Lcom/mojang/serialization/Codec;")
    @Expression("CODEC = @(?)")
    @ModifyExpressionValue(method = "<clinit>", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static Codec<StatePropertiesPredicate.ValueMatcher> wrapCodec(Codec<StatePropertiesPredicate.ValueMatcher> original) {
        Codec<StatePropertiesPredicate.ValueMatcher> codec = DimensionValueMatcher.TYPE_REGISTRY.byNameCodec()
                .dispatch(IdUtils.idString("type"), DimensionValueMatcher::codec, Function.identity())
                .flatComapMap(Function.identity(), valueMatcher -> valueMatcher instanceof DimensionValueMatcher dimension
                        ? DataResult.success(dimension) : DataResult.error(() -> "Value matcher is not a dimension matcher."));
        return NeoForgeExtraCodecs.withAlternative(codec, original);
    }
}
