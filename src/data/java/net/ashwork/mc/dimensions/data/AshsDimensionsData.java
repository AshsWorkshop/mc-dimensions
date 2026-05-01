package net.ashwork.mc.dimensions.data;

import net.ashwork.mc.dimensions.AshsDimensions;
import net.ashwork.mc.dimensions.data.client.DimensionsLanguageProvider;
import net.ashwork.mc.dimensions.data.client.DimensionsModelProvider;
import net.ashwork.mc.dimensions.data.server.DimensionsRecipeProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(AshsDimensions.ID)
public class AshsDimensionsData {

    public AshsDimensionsData(IEventBus modBus) {
        modBus.addListener(AshsDimensionsData::gatherData);
    }

    private static void gatherData(GatherDataEvent.Client event) {
        var pack = event.getGenerator().getPackGenerator(true, AshsDimensions.ID, "");

        // Client providers
        pack.addProvider(DimensionsLanguageProvider::new);
        pack.addProvider(DimensionsModelProvider::new);

        // Server providers
        pack.addProvider(output -> new DimensionsRecipeProvider.Runner(output, event.getLookupProvider()));
    }
}
