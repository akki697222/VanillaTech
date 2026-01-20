package akki697222.vanillatech.api.common.block.machine;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.common.CapabilityProviderHolder;
import akki697222.vanillatech.api.common.IEnergyStorageProvider;
import akki697222.vanillatech.api.common.IFluidHandlerProvider;
import akki697222.vanillatech.api.common.IItemStackHandlerHolder;
import akki697222.vanillatech.api.common.energy.SimpleEnergyStorage;
import akki697222.vanillatech.api.common.fluid.FluidHandler;
import akki697222.vanillatech.api.common.fluid.SingleFluidHandler;
import akki697222.vanillatech.api.common.fluid.VTFluidHandler;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
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
import java.util.function.Consumer;

public abstract class MachineBlockEntity extends BlockEntity implements Container, MenuProvider, BlockEntityTicker<MachineBlockEntity>, IEnergyStorageProvider, IFluidHandlerProvider, IItemStackHandlerHolder {
    protected IEnergyStorage energyStorage;
    protected VTFluidHandler fluidHandler;
    protected ContainerData containerData;
    protected ItemStackHandler inventory;
    protected boolean isActive = false;
    protected final boolean isSlave;
    protected boolean isSynced = false;
    protected final List<MachineInterface> machineInterfaces = new ArrayList<>();
    private BlockPos masterPos;

    public MachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, IEnergyStorage energyStorage, VTFluidHandler fluidHandler, int containerCapacity, int additionalData) {
        super(type, pos, blockState);
        this.energyStorage = energyStorage;
        this.fluidHandler = fluidHandler;
        this.containerData = new SimpleContainerData(containerCapacity + additionalData);
        this.containerData.set(0, energyStorage.getEnergyStored());
        this.containerData.set(1, energyStorage.getMaxEnergyStored());
        this.isSlave = blockState.getValue(MachineBlock.SLAVE);
        this.inventory = new ItemStackHandler(containerCapacity) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    public MachineInterface getInterface(Direction side, BlockPos blockPos, BlockState blockState, MachineInterface.InterfaceType type) {
        BlockPos masterPos = getMasterPos() != null && blockState.getValue(MachineBlock.SLAVE) ? getMasterPos() : blockPos;
        for (MachineInterface machineInterface : machineInterfaces) {
            BlockPos adjustedPos = MachineBlock.adjustPos(
                    masterPos,
                    BlockPos.ZERO,
                    blockState.getValue(MachineBlock.FACING),
                    machineInterface.offset().getX(),
                    machineInterface.offset().getY(),
                    machineInterface.offset().getZ());

            boolean isThis = masterPos != null && adjustedPos.equals(blockPos);

            if (isThis) {
                Direction direction = MachineInterface.InterfaceSide.getFacingFromSide(
                        blockState.getValue(MachineBlock.FACING),
                        machineInterface.side());

                if (direction.equals(side)) {
                    if (type == machineInterface.type()) {
                        return machineInterface;
                    }
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T> CapabilityProviderHolder<T> getPort(Direction side, BlockPos blockPos, BlockState blockState, MachineInterface.InterfaceType type) {
        BlockPos masterPos = getMasterPos() != null && blockState.getValue(MachineBlock.SLAVE) ? getMasterPos() : blockPos;
        for (MachineInterface machineInterface : machineInterfaces) {
            BlockPos adjustedPos = MachineBlock.adjustPos(
                    masterPos,
                    BlockPos.ZERO,
                    blockState.getValue(MachineBlock.FACING),
                    machineInterface.offset().getX(),
                    machineInterface.offset().getY(),
                    machineInterface.offset().getZ());

            boolean isThis = masterPos != null && adjustedPos.equals(blockPos);

            if (isThis) {
                Direction direction = MachineInterface.InterfaceSide.getFacingFromSide(
                        blockState.getValue(MachineBlock.FACING),
                        machineInterface.side());

                if (direction.equals(side)) {
                    if (type == machineInterface.type()) {
                        return switch (type) {
                            case ENERGY -> new CapabilityProviderHolder<>((T) getEnergyStorage());
                            case FLUID -> new CapabilityProviderHolder<>((T) getFluidHandler());
                            case ITEM -> new CapabilityProviderHolder<>((T) getInventory());
                            case null -> null;
                        };
                    }
                }
            }
        }
        return null;
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
        if (masterPos != null) {
            tag.putLong("master", masterPos.asLong());
        }

        if (fluidHandler instanceof SingleFluidHandler single) {
            encodeCodec("FluidHandler", SingleFluidHandler.CODEC, single, tag);
        } else if (fluidHandler instanceof FluidHandler multi) {
            encodeCodec("FluidHandler", FluidHandler.CODEC, multi, tag);
        }

        if (energyStorage instanceof SimpleEnergyStorage simple) {
            encodeCodec("EnergyStorage", SimpleEnergyStorage.CODEC, simple, tag);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);

        isSynced = false;

        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        if (tag.contains("master")) {
            masterPos = BlockPos.of(tag.getLong("master"));
        }

        if (fluidHandler instanceof SingleFluidHandler single) {
            decodeCodec("FluidHandler", SingleFluidHandler.CODEC, tag, f -> {
                single.setFluid(f.getFluid());
                single.setCapacity(f.getCapacity());
                single.setMaxTransfer(f.getMaxTransfer());
            });
        } else if (fluidHandler instanceof FluidHandler handler) {
            decodeCodec("FluidHandler", FluidHandler.CODEC, tag, decodedHandler -> {
                List<FluidHandler.Tank> currentTanks = handler.getAllTanks();
                List<FluidHandler.Tank> decodedTanks = decodedHandler.getAllTanks();

                for (int i = 0; i < currentTanks.size(); i++) {
                    if (i < decodedTanks.size()) {
                        FluidHandler.Tank dt = decodedTanks.get(i);
                        handler.setFluidInTank(i, dt.getFluid());
                    }
                }
            });
        }

        if (energyStorage instanceof SimpleEnergyStorage) {
            decodeCodec("EnergyStorage", SimpleEnergyStorage.CODEC, tag, e -> {
                e.updateMaxEnergy(e.getMaxEnergyStored());
                e.updateEnergyStored(e.getEnergyStored());
                e.updateMaxReceive(e.getMaxReceive());
                e.updateMaxExtract(e.getMaxExtract());
            });
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        loadAdditional(tag, registries);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private <T> void encodeCodec(String name, Codec<T> codec, T handler, CompoundTag rootTag) {
        codec.encodeStart(NbtOps.INSTANCE, handler)
                .resultOrPartial(error -> VanillaTech.LOGGER.warn("Failed to encode {}: {}", name, error))
                .ifPresent(nbt -> {
                    if (nbt instanceof CompoundTag compound) {
                        rootTag.put(name, compound);
                    } else {
                        VanillaTech.LOGGER.warn("Expected CompoundTag but got {}", nbt.getType().getName());
                    }
                });
    }

    private <T> void decodeCodec(String name, Codec<T> codec, CompoundTag rootTag, Consumer<T> setter) {
        if (rootTag.contains(name, Tag.TAG_COMPOUND)) {
            codec.parse(NbtOps.INSTANCE, rootTag.getCompound(name))
                    .resultOrPartial(error -> VanillaTech.LOGGER.warn("Failed to decode {}: {}", name, error))
                    .ifPresent(setter);
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

                this.energyStorage = master.getEnergyStorage();
                this.fluidHandler = master.getFluidHandler();
                this.containerData = master.getContainerData();
                this.inventory = master.getInventory();
            }

            isSynced = true;
        }

        if (isActive != blockState.getValue(MachineBlock.ACTIVE) && !blockState.getValue(MachineBlock.SLAVE)) {
            level.setBlock(blockPos, blockState.setValue(MachineBlock.ACTIVE, isActive), 2);
        }

        this.containerData.set(0, energyStorage.getEnergyStored());
        this.containerData.set(1, energyStorage.getMaxEnergyStored());
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public VTFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    @Override
    public ItemStackHandler getItemStackHandler() {
        return inventory;
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    @Override
    public int getContainerSize() {
        return inventory.getSlots();
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
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return null;
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    public boolean getIsActive() {
        return isActive;
    }
}
