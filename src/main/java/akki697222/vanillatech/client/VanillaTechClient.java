package akki697222.vanillatech.client;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.client.gui.screen.machine.ArcFurnaceScreen;
import akki697222.vanillatech.common.VTBlockEntities;
import akki697222.vanillatech.common.VTMenuTypes;
import akki697222.vanillatech.common.VTPartialModels;
import akki697222.vanillatech.client.renderer.machine.ArcFurnaceRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, modid = VanillaTech.MODID, bus = EventBusSubscriber.Bus.MOD)
public class VanillaTechClient {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(VTBlockEntities.ARC_FURNACE.get(), ArcFurnaceRenderer::new);
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(VTMenuTypes.ARC_FURNACE.get(), ArcFurnaceScreen::new);
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(VTPartialModels.ARC_FURNACE_ELECTRODE);
        event.register(VTPartialModels.ARC_FURNACE_ELECTRODE_ACTIVE);
    }
}
