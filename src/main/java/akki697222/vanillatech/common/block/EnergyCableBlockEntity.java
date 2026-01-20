package akki697222.vanillatech.common.block;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.common.IEnergyStorageProvider;
import akki697222.vanillatech.api.common.block.cable.CableBlockEntity;
import akki697222.vanillatech.api.common.block.machine.MachineBlockEntity;
import akki697222.vanillatech.api.common.block.machine.MachineInterface;
import akki697222.vanillatech.api.common.energy.SimpleEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class EnergyCableBlockEntity extends CableBlockEntity<EnergyCableBlockEntity> implements IEnergyStorageProvider {
    protected final int maxEnergyTransfer;
    protected final SimpleEnergyStorage simpleEnergyStorage;

    public EnergyCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int maxEnergyTransfer) {
        super(type, pos, blockState);

        this.maxEnergyTransfer = maxEnergyTransfer;
        this.simpleEnergyStorage = new SimpleEnergyStorage(4000, 4000, maxEnergyTransfer);
    }

    @Override
    public IEnergyStorage getEnergyStorage() {
        return simpleEnergyStorage;
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull EnergyCableBlockEntity blockEntity) {
        if (level.isClientSide) return;

        List<EnergyCableBlockEntity> network = collectCableNetwork(level, pos);

        if (!network.isEmpty() && network.getFirst() == this) {
            equalizeEnergy(network);

            for (EnergyCableBlockEntity cable : network) {
                cable.distributeToConsumers(level, cable.getBlockPos());
            }
        }
    }

    private List<EnergyCableBlockEntity> collectCableNetwork(Level level, BlockPos pos) {
        List<EnergyCableBlockEntity> cables = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        collectCablesRecursive(level, pos, cables, visited);
        return cables;
    }

    private void collectCablesRecursive(Level level, BlockPos pos, List<EnergyCableBlockEntity> cables, Set<BlockPos> visited) {
        if (!visited.add(pos)) return;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof EnergyCableBlockEntity cable) {
            cables.add(cable);
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.relative(direction);
                BlockEntity neighbor = level.getBlockEntity(neighborPos);
                if (neighbor instanceof EnergyCableBlockEntity) {
                    collectCablesRecursive(level, neighborPos, cables, visited);
                }
            }
        }
    }

    private void equalizeEnergy(List<EnergyCableBlockEntity> cables) {
        if (cables.size() <= 1) return;

        long totalEnergy = 0;
        long totalCapacity = 0;

        for (EnergyCableBlockEntity cable : cables) {
            totalEnergy += cable.simpleEnergyStorage.getEnergyStored();
            totalCapacity += cable.simpleEnergyStorage.getMaxEnergyStored();
        }

        if (totalEnergy == 0) return;

        double fillRatio = (double) totalEnergy / totalCapacity;
        long remainingEnergy = totalEnergy;

        for (int i = 0; i < cables.size(); i++) {
            EnergyCableBlockEntity cable = cables.get(i);
            int myCapacity = cable.simpleEnergyStorage.getMaxEnergyStored();

            int targetEnergy;
            if (i == cables.size() - 1) {
                targetEnergy = (int) remainingEnergy;
            } else {
                targetEnergy = (int) Math.floor(myCapacity * fillRatio);
                remainingEnergy -= targetEnergy;
            }

            int current = cable.simpleEnergyStorage.getEnergyStored();
            if (targetEnergy > current) {
                cable.simpleEnergyStorage.receiveEnergy(targetEnergy - current, false);
            } else {
                cable.simpleEnergyStorage.extractEnergy(current - targetEnergy, false);
            }
        }
    }

    private void distributeToConsumers(Level level, BlockPos pos) {
        int currentEnergy = simpleEnergyStorage.getEnergyStored();
        if (currentEnergy <= 0) return;

        List<Consumer> consumers = getConsumers(level, pos);
        consumers.sort(Comparator.comparingDouble(c -> c.pos.distSqr(pos)));

        for (Consumer consumer : consumers) {
            if (consumer.storage.canReceive() && consumer.canTransfer) {
                int toTransfer = Math.min(currentEnergy, maxEnergyTransfer);
                int received = consumer.storage.receiveEnergy(toTransfer, false);
                if (received > 0) {
                    simpleEnergyStorage.extractEnergy(received, false);
                    VanillaTech.LOGGER.info("Transferred {} FE from {} to {}", received, pos, consumer.pos);
                    currentEnergy = simpleEnergyStorage.getEnergyStored();
                    if (currentEnergy <= 0) break;
                }
            }
        }
    }

    private static class Consumer {
        BlockPos pos;
        IEnergyStorage storage;
        boolean canTransfer; // 搬入可能かどうか

        Consumer(BlockPos pos, IEnergyStorage storage, boolean canTransfer) {
            this.pos = pos;
            this.storage = storage;
            this.canTransfer = canTransfer;
        }
    }

    private List<Consumer> getConsumers(Level level, BlockPos pos) {
        List<Consumer> consumers = new ArrayList<>();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof EnergyCableBlock cableBlock)) return consumers;

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            if (cableBlock.canConnectTo(level, neighborPos, direction)) {
                IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, direction.getOpposite());
                if (storage != null && !(level.getBlockEntity(neighborPos) instanceof EnergyCableBlockEntity)) {
                    boolean canTransfer = true;
                    BlockEntity be = level.getBlockEntity(neighborPos);
                    if (be instanceof MachineBlockEntity machine) {
                        MachineInterface iface = machine.getInterface(direction.getOpposite(), neighborPos, level.getBlockState(neighborPos), MachineInterface.InterfaceType.ENERGY);
                        if (iface != null) {
                            // INまたはBOTHなら搬入可能
                            canTransfer = iface.ioType() == MachineInterface.InterfaceIO.IN || iface.ioType() == MachineInterface.InterfaceIO.BOTH;
                        } else {
                            canTransfer = false; // インターフェースがない場合は転送しない
                        }
                    }
                    consumers.add(new Consumer(neighborPos, storage, canTransfer));
                }
            }
        }
        return consumers;
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);

        simpleEnergyStorage.receiveEnergy(tag.getInt("energy"), false);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putInt("energy", simpleEnergyStorage.getEnergyStored());
    }
}
