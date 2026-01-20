package akki697222.vanillatech.api.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record EnergySyncPayload(BlockPos pos, int energyStored, int maxEnergy, int maxReceive, int maxExtract) implements CustomPacketPayload {
    public static final Type<EnergySyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("vanillatech", "energy_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, EnergySyncPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, EnergySyncPayload::pos,
            ByteBufCodecs.VAR_INT, EnergySyncPayload::energyStored,
            ByteBufCodecs.VAR_INT, EnergySyncPayload::maxEnergy,
            ByteBufCodecs.VAR_INT, EnergySyncPayload::maxReceive,
            ByteBufCodecs.VAR_INT, EnergySyncPayload::maxExtract,
            EnergySyncPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}