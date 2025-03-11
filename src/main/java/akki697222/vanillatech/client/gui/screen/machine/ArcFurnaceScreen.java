package akki697222.vanillatech.client.gui.screen.machine;

import akki697222.vanillatech.api.VanillaTechAPI;
import akki697222.vanillatech.api.client.gui.screen.MachineScreen;
import akki697222.vanillatech.common.machines.arcfurnace.ArcFurnaceMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class ArcFurnaceScreen extends MachineScreen<ArcFurnaceMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = VanillaTechAPI.location("textures/gui/arc_furnace_gui.png");
    private static final ResourceLocation BURN_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");

    public ArcFurnaceScreen(ArcFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(CONTAINER_BACKGROUND, x, y, 0, 0, imageWidth, imageHeight);

        int progressWidth = Mth.ceil(menu.getProgressScaled() * 24.0F);
        guiGraphics.blitSprite(BURN_PROGRESS_SPRITE, 24, 16, 0, 0, leftPos + 88, topPos + 35, progressWidth, 16);

        super.renderBg(guiGraphics, v, i, i1);
    }
}
