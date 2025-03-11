package akki697222.vanillatech.api.client.gui.screen;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.VanillaTechAPI;
import akki697222.vanillatech.api.common.menu.MachineMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public abstract class MachineScreen<T extends MachineMenu> extends AbstractContainerScreen<T> {
    public static final ResourceLocation ENERGY_FILL = VanillaTechAPI.location("textures/gui/misc/energy_bar_fill.png");

    protected MachineScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        this.titleLabelX = (imageWidth / 2) - (font.width(title) / 2);
        this.titleLabelY = 5;

        super.renderLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        int energyStored = menu.getEnergyStored();
        int maxEnergy = menu.getMaxEnergy();
        int gaugeHeight = (int) ((energyStored / (float) maxEnergy) * 41);

        Minecraft.getInstance().getTextureManager().bindForSetup(ENERGY_FILL);

        int x = leftPos + (imageWidth / 2) + 66;
        int y = topPos + 7;
        guiGraphics.blit(
                ENERGY_FILL,
                x, y + (41 - gaugeHeight),
                0, 41 - gaugeHeight,
                12, gaugeHeight,
                12, 41
        );
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);

        int barX = leftPos + (imageWidth / 2) + 66;
        int barY = topPos + 7;
        int barWidth = 12;
        int barHeight = 41;

        if (mouseX >= barX && mouseX < barX + barWidth && mouseY >= barY && mouseY < barY + barHeight) {
            int energyStored = menu.getEnergyStored();
            int maxEnergy = menu.getMaxEnergy();

            guiGraphics.renderTooltip(font,
                    Component.literal(energyStored + " / " + maxEnergy + "FE"),
                    mouseX,
                    mouseY
            );
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
