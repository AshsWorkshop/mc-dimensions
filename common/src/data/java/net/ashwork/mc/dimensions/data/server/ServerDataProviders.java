package net.ashwork.mc.dimensions.data.server;

import net.ashwork.mc.multiloader.api.data.generator.DataProviderGatherer;

public interface ServerDataProviders {

    static void gather(DataProviderGatherer gatherer) {
        gatherer.add(DimensionsRecipeProvider.Runner::new);
    }
}
