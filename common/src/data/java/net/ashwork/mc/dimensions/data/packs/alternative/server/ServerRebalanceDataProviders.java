package net.ashwork.mc.dimensions.data.packs.alternative.server;

import net.ashwork.mc.dimensions.data.server.DimensionsRecipeProvider;
import net.ashwork.mc.multiloader.api.data.generator.DataProviderGatherer;

public interface ServerRebalanceDataProviders {

    static void gather(DataProviderGatherer gatherer) {
        gatherer.add(RebalanceRecipeProvider.Runner::new);
    }
}
