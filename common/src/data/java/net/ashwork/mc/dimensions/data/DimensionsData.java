package net.ashwork.mc.dimensions.data;

import net.ashwork.mc.dimensions.Dimensions;
import net.ashwork.mc.dimensions.data.client.ClientDataProviders;
import net.ashwork.mc.dimensions.data.server.ServerDataProviders;
import net.ashwork.mc.multiloader.api.base.ModLoader;
import net.ashwork.mc.multiloader.api.data.DataModLoaderAccessor;
import net.ashwork.mc.multiloader.api.data.generator.PackFactory;

public interface DimensionsData {

    ModLoader PLATFORM = DataModLoaderAccessor.INSTANCE.create(Dimensions.ID);

    static void init() {
        PLATFORM.access(PackFactory.EXT).global()
                .gatherProviders(ClientDataProviders::gather)
                .gatherProviders(ServerDataProviders::gather);
    }
}
