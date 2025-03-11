package akki697222.vanillatech.api.common.network;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.VanillaTechAPI;
import akki697222.vanillatech.api.common.menu.MachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record MachineSyncPacket(int energyStored, int maxEnergy, BlockPos entityPos) implements CustomPacketPayload {
    public static final ResourceLocation ID = VanillaTechAPI.location("machine_sync");
    public static final Type<MachineSyncPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, MachineSyncPacket> CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeInt(packet.energyStored);
                buf.writeInt(packet.maxEnergy);
                buf.writeBlockPos(packet.entityPos);
            },
            buf -> new MachineSyncPacket(
                    buf.readInt(),
                    buf.readInt(),
                    buf.readBlockPos()
            )
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MachineSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player.containerMenu instanceof MachineMenu machineMenu) {

            } else {
                VanillaTech.LOGGER.info(player.containerMenu.getClass().getSimpleName());
            }
        });
    }
}