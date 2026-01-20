package akki697222.vanillatech.api.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public record FluidSyncPayload(BlockPos pos, FluidStack stack, int slot) implements CustomPacketPayload {
    public static final Type<FluidSyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("vanillatech", "fluid_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidSyncPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, FluidSyncPayload::pos,
            FluidStack.STREAM_CODEC, FluidSyncPayload::stack,
            ByteBufCodecs.VAR_INT, FluidSyncPayload::slot,
            FluidSyncPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}