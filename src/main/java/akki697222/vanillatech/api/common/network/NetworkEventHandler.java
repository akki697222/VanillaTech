package akki697222.vanillatech.api.common.network;

import akki697222.vanillatech.VanillaTech;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = VanillaTech.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkEventHandler {
    @SubscribeEvent
    public static void onRegisterPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0")
                .versioned("1.0")
                .optional();

        registrar.playToClient(
                MachineSyncPacket.TYPE,
                MachineSyncPacket.CODEC,
                MachineSyncPacket::handle
        );
    }
}
