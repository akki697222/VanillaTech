package akki697222.vanillatech.api.common.block.machine;

import akki697222.vanillatech.api.common.IEnergyStorageProvider;
import akki697222.vanillatech.api.common.energy.SimpleEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static akki697222.vanillatech.common.machines.arcfurnace.ArcFurnaceBlockEntity.SLOT_NUM;

public abstract class MachineBlockEntity extends BlockEntity implements Container, MenuProvider, BlockEntityTicker<MachineBlockEntity>, IEnergyStorageProvider {
    protected SimpleEnergyStorage simpleEnergyStorage;
    protected ContainerData containerData;
    protected ItemStackHandler inventory = new ItemStackHandler(SLOT_NUM) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    protected boolean isActive = false;
    protected final boolean isSlave;
    protected boolean isSynced = false;
    protected final List<MachineInterface> machineInterfaces = new ArrayList<>();
    private BlockPos masterPos;

    public MachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, SimpleEnergyStorage simpleEnergyStorage, int additionalData) {
        super(type, pos, blockState);
        this.simpleEnergyStorage = simpleEnergyStorage;
        this.containerData = new SimpleContainerData(2 + additionalData);
        this.containerData.set(0, simpleEnergyStorage.getEnergyStored());
        this.containerData.set(1, simpleEnergyStorage.getMaxEnergyStored());
        this.isSlave = blockState.getValue(MachineBlock.SLAVE);
    }

    public List<MachineInterface> getMachineInterfaces() {
        return machineInterfaces;
    }

    public void setMasterPos(BlockPos masterPos) {
        this.masterPos = masterPos;
    }

    public BlockPos getMasterPos() {
        return masterPos;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);

        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("energy", simpleEnergyStorage.getEnergyStored());
        if (masterPos != null) {
            tag.putLong("master", masterPos.asLong());
        }
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);

        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        simpleEnergyStorage.receiveEnergy(tag.getInt("energy"), false);
        if (tag.contains("master")) {
            masterPos = BlockPos.of(tag.getLong("master"));
        }
    }

    public MachineBlockEntity getMaster(@NotNull Level level) {
        BlockEntity be = level.getBlockEntity(getMasterPos());

        if (be instanceof MachineBlockEntity machineBlockEntity) {
            return machineBlockEntity;
        } else {
            throw new IllegalStateException("Failed to find master.");
        }
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull MachineBlockEntity machineBlockEntity) {
        if (level.isClientSide) return;

        if (!isSynced) {
            if (getBlockState().getValue(MachineBlock.SLAVE)) {
                MachineBlockEntity master = getMaster(level);

                this.simpleEnergyStorage = (SimpleEnergyStorage) master.getEnergyStorage();
                this.containerData = master.getContainerData();
                this.inventory = master.getInventory();
            }

            isSynced = true;
        }

        if (isActive != blockState.getValue(MachineBlock.ACTIVE) && !blockState.getValue(MachineBlock.SLAVE)) {
            level.setBlock(blockPos, blockState.setValue(MachineBlock.ACTIVE, isActive), 2);
        }

        this.containerData.set(0, simpleEnergyStorage.getEnergyStored());
        this.containerData.set(1, simpleEnergyStorage.getMaxEnergyStored());
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public IEnergyStorage getEnergyStorage() {
        return simpleEnergyStorage;
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack stack = inventory.extractItem(slot, amount, false);
        setChanged();
        return stack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = inventory.extractItem(slot, inventory.getStackInSlot(slot).getCount(), false);
        setChanged();
        return stack;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        inventory.setStackInSlot(slot, stack);
        setChanged();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
        setChanged();
    }

    @Override
    public abstract @NotNull Component getDisplayName();

    @Override
    public abstract @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player);

    @Override
    public void setChanged() {
        super.setChanged();
    }

    public boolean getIsActive() {
        return isActive;
    }
}
