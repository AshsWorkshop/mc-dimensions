package net.ashwork.mc.dimensions.network;

import io.netty.buffer.ByteBuf;
import net.ashwork.mc.dimensions.util.IdUtils;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.List;

public record ClientboundFlipAdvancementRequirementsDataPayload(List<Identifier> advancements) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientboundFlipAdvancementRequirementsDataPayload> TYPE = IdUtils.packet("flip_advancement_requirements_data");
    public static final StreamCodec<ByteBuf, ClientboundFlipAdvancementRequirementsDataPayload> STREAM_CODEC = Identifier.STREAM_CODEC
            .apply(ByteBufCodecs.list()).map(ClientboundFlipAdvancementRequirementsDataPayload::new, ClientboundFlipAdvancementRequirementsDataPayload::advancements);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
