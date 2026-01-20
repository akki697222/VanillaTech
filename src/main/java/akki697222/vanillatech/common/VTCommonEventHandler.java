package akki697222.vanillatech.common;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.client.network.ClientNetworkPayloadHandler;
import akki697222.vanillatech.api.common.network.EnergySyncPayload;
import akki697222.vanillatech.api.common.network.FluidSyncPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = VanillaTech.MODID, bus = EventBusSubscriber.Bus.MOD)
public class VTCommonEventHandler {
    @SubscribeEvent
    public static void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(VanillaTech.MODID).versioned("1.0.0");

        registrar.playToClient(
                FluidSyncPayload.TYPE,
                FluidSyncPayload.STREAM_CODEC,
                ClientNetworkPayloadHandler::handleFluidSync
        );

        registrar.playToClient(
                EnergySyncPayload.TYPE,
                EnergySyncPayload.STREAM_CODEC,
                ClientNetworkPayloadHandler::handleEnergySync
        );
    }
}
