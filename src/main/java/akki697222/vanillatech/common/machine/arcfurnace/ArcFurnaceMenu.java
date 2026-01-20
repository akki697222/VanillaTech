package akki697222.vanillatech.common.machine.arcfurnace;

import akki697222.vanillatech.api.common.energy.SimpleEnergyStorage;
import akki697222.vanillatech.api.common.fluid.SingleFluidHandler;
import akki697222.vanillatech.api.common.menu.FilteredSlot;
import akki697222.vanillatech.api.common.menu.MachineMenu;
import akki697222.vanillatech.api.common.menu.OutputSlot;
import akki697222.vanillatech.common.VTMenuTypes;
import akki697222.vanillatech.common.item.ElectrodeItem;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ArcFurnaceMenu extends MachineMenu {
    public ArcFurnaceMenu(int containerId, Inventory playerInventory, Container container, ContainerData containerData, SimpleEnergyStorage simpleEnergyStorage) {
        super(VTMenuTypes.ARC_FURNACE.get(), containerId, playerInventory, container, containerData, simpleEnergyStorage, new SingleFluidHandler(0, 0), true);

        addMachineSlot();
    }

    public ArcFurnaceMenu(int containerId, Inventory playerInventory) {
        super(VTMenuTypes.ARC_FURNACE.get(), containerId, playerInventory, new SimpleContainer(ArcFurnaceBlockEntity.SLOT_NUM), new SimpleContainerData(ArcFurnaceBlockEntity.SLOT_NUM + 2), new SimpleEnergyStorage(0, 1000, 0), new SingleFluidHandler(0, 0), true);

        addMachineSlot();
    }

    private void addMachineSlot() {
        this.addSlot(new FilteredSlot(machineContainer, ArcFurnaceBlockEntity.SLOT_ELECTRODE, 47, 15, ElectrodeItem.class));
        this.addSlot(new Slot(machineContainer, ArcFurnaceBlockEntity.SLOT_INPUT_1, 29, 35));
        this.addSlot(new Slot(machineContainer, ArcFurnaceBlockEntity.SLOT_INPUT_2, 47, 35));
        this.addSlot(new Slot(machineContainer, ArcFurnaceBlockEntity.SLOT_ADDITIVE, 65, 35));
        this.addSlot(new OutputSlot(machineContainer, ArcFurnaceBlockEntity.SLOT_SLAG, 125, 53));
        this.addSlot(new OutputSlot(machineContainer, ArcFurnaceBlockEntity.SLOT_OUTPUT, 125, 28));
        this.addSlot(new Slot(machineContainer, ArcFurnaceBlockEntity.SLOT_ENERGY, 152, 53));
    }

    public float getProgressScaled() {
        int progress = machineContainerData.get(2);
        int maxProgress = machineContainerData.get(3);
        return maxProgress > 0 ? (float) progress / maxProgress : 0.0F;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int quickMovedSlotIndex) {
        ItemStack originalStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(quickMovedSlotIndex);

        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            originalStack = stackInSlot.copy();

            final int machineSlotsStart = 0;
            final int machineSlotsEnd = ArcFurnaceBlockEntity.SLOT_NUM;
            final int inventoryEnd = machineSlotsEnd + 27;
            final int hotbarEnd = inventoryEnd + 9;

            if (quickMovedSlotIndex >= machineSlotsStart && quickMovedSlotIndex < machineSlotsEnd) {
                if (!this.moveItemStackTo(stackInSlot, machineSlotsEnd, hotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (stackInSlot.getItem() instanceof ElectrodeItem) {
                    if (!this.moveItemStackTo(stackInSlot, ArcFurnaceBlockEntity.SLOT_ELECTRODE, ArcFurnaceBlockEntity.SLOT_ELECTRODE + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (machineContainer.canPlaceItem(ArcFurnaceBlockEntity.SLOT_ENERGY, stackInSlot)) {
                    if (!this.moveItemStackTo(stackInSlot, ArcFurnaceBlockEntity.SLOT_ENERGY, ArcFurnaceBlockEntity.SLOT_ENERGY + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else {
                    if (!this.moveItemStackTo(stackInSlot, ArcFurnaceBlockEntity.SLOT_INPUT_1, ArcFurnaceBlockEntity.SLOT_ADDITIVE + 1, false)) {
                        if (quickMovedSlotIndex < inventoryEnd &&
                                !this.moveItemStackTo(stackInSlot, inventoryEnd, hotbarEnd, false)) {
                            return ItemStack.EMPTY;
                        } else if (quickMovedSlotIndex >= inventoryEnd &&
                                !this.moveItemStackTo(stackInSlot, machineSlotsEnd, inventoryEnd, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == originalStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }

        return originalStack;
    }
}
