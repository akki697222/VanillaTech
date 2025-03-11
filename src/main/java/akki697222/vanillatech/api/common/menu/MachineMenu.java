package akki697222.vanillatech.api.common.menu;

import akki697222.vanillatech.api.common.energy.SimpleEnergyStorage;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class MachineMenu extends AbstractContainerMenu {
    protected final SimpleEnergyStorage energyStorage;
    protected final Container machineContainer;
    protected final ContainerData machineContainerData;
    protected final Player player;

    public MachineMenu(MenuType<? extends MachineMenu> menuType, int containerId, Inventory playerInventory, Container container, ContainerData containerData, SimpleEnergyStorage simpleEnergyStorage) {
        super(menuType, containerId);
        this.energyStorage = simpleEnergyStorage;
        this.machineContainer = container;
        this.machineContainerData = containerData;
        this.player = playerInventory.player;

        addInventorySlot(playerInventory);

        this.addDataSlots(containerData);
    }

    public int getEnergyStored() {
        return machineContainerData.get(0);
    }

    public int getMaxEnergy() {
        return machineContainerData.get(1);
    }

    protected void addInventorySlot(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for(int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    public SimpleEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public abstract @NotNull ItemStack quickMoveStack(@NotNull Player player, int quickMovedSlotIndex);

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
