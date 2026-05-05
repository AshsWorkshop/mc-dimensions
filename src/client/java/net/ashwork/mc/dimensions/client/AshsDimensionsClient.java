package net.ashwork.mc.dimensions.client;

import net.ashwork.mc.dimensions.AshsDimensions;
import net.ashwork.mc.dimensions.client.model.ExpandedItemModelGeneratorLoader;
import net.ashwork.mc.dimensions.client.neoext.ClientExtensions;
import net.ashwork.mc.dimensions.client.neoext.ModelExtensions;
import net.ashwork.mc.dimensions.client.network.ClientPayloadRegistrar;
import net.ashwork.mc.dimensions.tags.DimensionItemTags;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(AshsDimensions.ID)
public class AshsDimensionsClient {

    public AshsDimensionsClient(IEventBus modBus) {
        ClientExtensions.register(modBus);
        ClientPayloadRegistrar.register(modBus);
        ModelExtensions.setup();
        modBus.addListener(AshsDimensionsClient::registerLoaders);
    }

    private static void registerLoaders(ModelEvent.RegisterLoaders event) {
        event.register(ExpandedItemModelGeneratorLoader.ID, ExpandedItemModelGeneratorLoader.INSTANCE);
    }
}
