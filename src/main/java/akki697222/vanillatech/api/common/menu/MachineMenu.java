package akki697222.vanillatech.api.common.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public abstract class MachineMenu extends AbstractContainerMenu {
    protected final IEnergyStorage energyStorage;
    protected final IFluidHandler fluidHandler;
    protected final Container machineContainer;
    protected final ContainerData machineContainerData;
    protected final Player player;
    protected final boolean hasEnergyBar;

    public MachineMenu(MenuType<? extends MachineMenu> menuType, int containerId, Inventory playerInventory, Container container, ContainerData containerData, IEnergyStorage energyStorage, IFluidHandler fluidHandler, boolean hasEnergyBar) {
        super(menuType, containerId);
        this.energyStorage = energyStorage;
        this.machineContainer = container;
        this.machineContainerData = containerData;
        this.player = playerInventory.player;
        this.fluidHandler = fluidHandler;
        this.hasEnergyBar = hasEnergyBar;

        addInventorySlot(playerInventory);

        this.addDataSlots(containerData);
    }

    public boolean hasEnergyBar() {
        return hasEnergyBar;
    }

    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
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

    @Override
    public abstract @NotNull ItemStack quickMoveStack(@NotNull Player player, int quickMovedSlotIndex);

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
