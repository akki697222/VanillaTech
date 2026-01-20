package akki697222.vanillatech.client.gui.screen.machine;

import akki697222.vanillatech.api.VanillaTechAPI;
import akki697222.vanillatech.api.client.gui.screen.MachineScreen;
import akki697222.vanillatech.common.machine.boiler.solidfuel.SolidFuelBoilerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class SolidFuelBoilerScreen extends MachineScreen<SolidFuelBoilerMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = VanillaTechAPI.location("textures/gui/solid_fuel_boiler_gui.png");

    public SolidFuelBoilerScreen(SolidFuelBoilerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        this.titleLabelX = (imageWidth / 2) - (font.width(title) / 2);
        this.titleLabelY = 5;

        super.renderLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(CONTAINER_BACKGROUND, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
