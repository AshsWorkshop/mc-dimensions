package net.ashwork.mc.dimensions.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.ashwork.mc.dimensions.storage.siftable.Siftable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.Optional;
import java.util.function.Consumer;

public record Sifting(ItemStack stack, EquipmentSlot slot, ResourceKey<LootTable> loot, int tickDuration, Optional<Holder<SoundEvent>> sound) {

    public static final Codec<Sifting> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("stack").forGetter(Sifting::stack),
            EquipmentSlot.CODEC.validate(
                    slot -> slot.getType() == EquipmentSlot.Type.HAND ? DataResult.success(slot) : DataResult.error(() -> "Sifting item must be in a hand slot")
            ).fieldOf("slot").forGetter(Sifting::slot),
            LootTable.KEY_CODEC.fieldOf("loot").forGetter(Sifting::loot),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("tick_duration").forGetter(Sifting::tickDuration),
            SoundEvent.CODEC.optionalFieldOf("sound").forGetter(Sifting::sound)
    ).apply(instance, Sifting::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, Sifting> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, Sifting::stack,
            EquipmentSlot.STREAM_CODEC, Sifting::slot,
            ResourceKey.streamCodec(Registries.LOOT_TABLE), Sifting::loot,
            ByteBufCodecs.INT, Sifting::tickDuration,
            ByteBufCodecs.optional(ByteBufCodecs.holderRegistry(Registries.SOUND_EVENT)), Sifting::sound,
            Sifting::new
    );

    public static Sifting create(ItemStack siftingItem, InteractionHand hand, ResourceKey<LootTable> loot, Optional<Holder<SoundEvent>> sound) {
        return new Sifting(siftingItem.copy(), hand.asEquipmentSlot(), loot, 200, sound);
    }

    public boolean canKeepUsing(LivingEntity entity) {
        var siftingItem = entity.getItemBySlot(this.slot());
        return ItemStack.isSameItemSameComponents(this.stack, siftingItem);
    }

    public void finishUsingItem(ItemStack sifter, ServerLevel level, LivingEntity entity) {
        // Shrink item
        entity.getItemBySlot(this.slot()).shrink(1);

        // Construct loot setup
        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, entity.position())
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(LootContextParams.TOOL, sifter)
                .create(Siftable.LOOT_CONTEXT);
        LootTable table = level.getServer().reloadableRegistries().getLootTable(this.loot);

        // Construct outputs
        Consumer<ItemStack> output = stack -> {
            if (!stack.isEmpty()) entity.drop(stack, false, false);
        };
        ResourceHandler<ItemResource> inventory = entity.getCapability(Capabilities.Item.ENTITY);
        if (inventory != null) {
            Consumer<ItemStack> firstInInventory = stack -> {
                try (var tx = Transaction.openRoot()) {
                    int numberInserted = ResourceHandlerUtil.insertStacking(inventory, ItemResource.of(stack), stack.getCount(), tx);
                    if (numberInserted > 0) {
                        tx.commit();
                        stack.shrink(numberInserted);
                    }
                }
            };
            output = firstInInventory.andThen(output);
        }

        // Drop loot
        table.getRandomItems(params, output);
    }
}
