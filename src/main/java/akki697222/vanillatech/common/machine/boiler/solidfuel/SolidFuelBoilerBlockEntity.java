package akki697222.vanillatech.common.machine.boiler.solidfuel;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.common.block.machine.MachineBlock;
import akki697222.vanillatech.api.common.block.machine.MachineBlockEntity;
import akki697222.vanillatech.api.common.block.machine.MachineInterface;
import akki697222.vanillatech.api.common.energy.SimpleEnergyStorage;
import akki697222.vanillatech.api.common.fluid.FluidHandler;
import akki697222.vanillatech.api.common.fluid.SingleFluidHandler;
import akki697222.vanillatech.common.VTBlockEntities;
import akki697222.vanillatech.common.VTFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static akki697222.vanillatech.api.common.block.machine.MachineBlock.ACTIVE;

public class SolidFuelBoilerBlockEntity extends MachineBlockEntity {
    public static final int SLOT_NUM = 1;
    private int burnTime;
    private int burnDuration;

    public SolidFuelBoilerBlockEntity(BlockPos pos, BlockState blockState) {
        super(VTBlockEntities.SOLID_FUEL_BOILER.get(),
                pos,
                blockState,
                new SimpleEnergyStorage(0, 0, 0),
                new FluidHandler(List.of(
                        new FluidHandler.FilteredTank(12000, 1000, Fluids.WATER),
                        new FluidHandler.FilteredTank(12000, 1000, VTFluids.STEAM.get())
                )),
                SLOT_NUM, 4);

        machineInterfaces.add(new MachineInterface(
                new BlockPos(-1, 1, 1),
                MachineInterface.InterfaceSide.LEFT,
                MachineInterface.InterfaceType.FLUID,
                MachineInterface.InterfaceIO.IN));

        machineInterfaces.add(new MachineInterface(
                new BlockPos(1, 2, 1),
                MachineInterface.InterfaceSide.RIGHT,
                MachineInterface.InterfaceType.FLUID,
                MachineInterface.InterfaceIO.OUT));

        machineInterfaces.add(new MachineInterface(
                new BlockPos(0, 0, 0),
                MachineInterface.InterfaceSide.FRONT,
                MachineInterface.InterfaceType.ITEM,
                MachineInterface.InterfaceIO.IN));
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("gui." + VanillaTech.MODID + ".boiler.title");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        super.createMenu(containerId, inventory, player);
        return new SolidFuelBoilerMenu(containerId, inventory, this, this.containerData, this.energyStorage, this.fluidHandler);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putInt("burnTime", burnTime);
        tag.putInt("burnDuration", burnDuration);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);

        burnTime = tag.getInt("burnTime");
        burnDuration = tag.getInt("burnDuration");
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull MachineBlockEntity machineBlockEntity) {
        super.tick(level, blockPos, blockState, machineBlockEntity);
        if (level.isClientSide) return;

        boolean wasActive = this.burnTime > 0;
        if (this.burnTime > 0) this.burnTime--;

        FluidHandler fluidHandler = (FluidHandler) this.fluidHandler;

        var waterTank = fluidHandler.getTank(0);
        var steamTank = fluidHandler.getTank(1);

        if (this.burnTime <= 0 && canConvert(waterTank, steamTank)) {
            ItemStack fuelStack = inventory.getStackInSlot(0);
            if (!fuelStack.isEmpty()) {
                int fuelValue = fuelStack.getBurnTime(null);
                if (fuelValue > 0) {
                    this.burnDuration = fuelValue;
                    this.burnTime = fuelValue;
                    fuelStack.shrink(1);
                    this.setChanged();
                }
            }
        }

        if (this.burnTime > 0 && canConvert(waterTank, steamTank)) {
            int conversionAmount = 10;

            waterTank.getFluid().shrink(conversionAmount);
            if (steamTank.getFluid().isEmpty()) {
                steamTank.setFluid(new FluidStack(VTFluids.STEAM.get(), conversionAmount));
            } else {
                steamTank.getFluid().grow(conversionAmount);
            }
            this.setChanged();
        }

        if (wasActive != (this.burnTime > 0)) {
            level.setBlock(blockPos, blockState.setValue(ACTIVE, this.burnTime > 0), 3);
        }

        pushSteamToOutputs(level, blockPos, blockState);
    }

    private void pushSteamToOutputs(Level level, BlockPos pos, BlockState state) {
        FluidHandler fluidHandler = (FluidHandler) this.fluidHandler;
        var steamTank = fluidHandler.getTank(1);
        if (steamTank.getFluid().isEmpty() || steamTank.getFluid().getAmount() <= 0) return;

        Direction facing = state.getValue(MachineBlock.FACING);

        BlockPos portPos = MachineBlock.adjustPos(pos, new BlockPos(1, 2, 1), facing, -1, 0, 0);

        Direction outDir = facing.getOpposite();
        BlockPos targetPos = portPos.relative(outDir);

        IFluidHandler neighbor = level.getCapability(Capabilities.FluidHandler.BLOCK, targetPos, outDir.getOpposite());
        if (neighbor != null) {
            FluidStack pushStack = steamTank.getFluid().copy();
            pushStack.setAmount(Math.min(pushStack.getAmount(), 1000));

            int accepted = neighbor.fill(pushStack, IFluidHandler.FluidAction.EXECUTE);
            if (accepted > 0) {
                steamTank.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
                this.setChanged();
            }
        }
    }

    private boolean canConvert(FluidHandler.Tank water, FluidHandler.Tank steam) {
        int amount = 10;
        return water.getFluid().getAmount() >= amount && (steam.getCapacity() - steam.getFluid().getAmount()) >= amount;
    }
}
