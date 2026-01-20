package akki697222.vanillatech.common.machine.boiler.solidfuel;

import akki697222.vanillatech.api.common.energy.SimpleEnergyStorage;
import akki697222.vanillatech.api.common.fluid.FluidHandler;
import akki697222.vanillatech.api.common.fluid.SingleFluidHandler;
import akki697222.vanillatech.api.common.menu.MachineMenu;
import akki697222.vanillatech.api.common.menu.TestedSlot;
import akki697222.vanillatech.common.VTFluids;
import akki697222.vanillatech.common.VTMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SolidFuelBoilerMenu extends MachineMenu {
    public SolidFuelBoilerMenu(int containerId, Inventory playerInventory, Container container, ContainerData containerData, IEnergyStorage energyStorage, IFluidHandler fluidHandler) {
        super(VTMenuTypes.SOLID_FUEL_BOILER.get(), containerId, playerInventory, container, containerData, energyStorage, fluidHandler, false);

        addMachineSlot();
    }

    public SolidFuelBoilerMenu(int containerId, Inventory playerInventory) {
        super(VTMenuTypes.SOLID_FUEL_BOILER.get(),
                containerId,
                playerInventory,
                new SimpleContainer(SolidFuelBoilerBlockEntity.SLOT_NUM),
                new SimpleContainerData(SolidFuelBoilerBlockEntity.SLOT_NUM + 4),
                new SimpleEnergyStorage(0, 0, 0),
                new FluidHandler(List.of(
                        new FluidHandler.FilteredTank(12000, 1000, Fluids.WATER),
                        new FluidHandler.FilteredTank(12000, 1000, VTFluids.STEAM.get())
                )),
                false);

        addMachineSlot();
    }

    private void addMachineSlot() {
        this.addSlot(new TestedSlot(machineContainer, 0, 80, 53, stack -> stack.getBurnTime(null) > 0));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int quickMovedSlotIndex) {
        return null;
    }
}
