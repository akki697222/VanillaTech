package akki697222.vanillatech.api.client.network;

import akki697222.vanillatech.api.common.IEnergyStorageProvider;
import akki697222.vanillatech.api.common.IFluidHandlerProvider;
import akki697222.vanillatech.api.common.energy.SimpleEnergyStorage;
import akki697222.vanillatech.api.common.fluid.FluidHandler;
import akki697222.vanillatech.api.common.fluid.SingleFluidHandler;
import akki697222.vanillatech.api.common.network.EnergySyncPayload;
import akki697222.vanillatech.api.common.network.FluidSyncPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@OnlyIn(Dist.CLIENT)
public final class ClientNetworkPayloadHandler {
    public static void handleFluidSync(final FluidSyncPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getBlockEntity(payload.pos()) instanceof IFluidHandlerProvider provider) {
                IFluidHandler fluidHandler = provider.getFluidHandler();
                if (fluidHandler instanceof FluidHandler fluidHandler_) {
                    fluidHandler_.getTank(payload.slot()).setFluid(payload.stack());
                } else if (fluidHandler instanceof SingleFluidHandler singleFluidHandler) {
                    singleFluidHandler.setFluid(payload.stack());
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    public static void handleEnergySync(final EnergySyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getBlockEntity(payload.pos()) instanceof IEnergyStorageProvider provider) {
                IEnergyStorage energyStorage = provider.getEnergyStorage();
                if (energyStorage instanceof SimpleEnergyStorage ses) {
                    ses.updateEnergyStored(payload.energyStored());
                    ses.updateMaxEnergy(payload.maxEnergy());
                    ses.updateMaxReceive(payload.maxReceive());
                    ses.updateMaxExtract(payload.maxExtract());
                } else {
                    energyStorage.receiveEnergy(payload.energyStored(), false);
                }
            }
        });
    }
}
