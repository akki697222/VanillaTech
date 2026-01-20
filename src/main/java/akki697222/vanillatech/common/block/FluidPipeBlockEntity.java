package akki697222.vanillatech.common.block;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.common.IFluidHandlerProvider;
import akki697222.vanillatech.api.common.block.cable.CableBlockEntity;
import akki697222.vanillatech.api.common.block.machine.MachineBlockEntity;
import akki697222.vanillatech.api.common.block.machine.MachineInterface;
import akki697222.vanillatech.api.common.fluid.SingleFluidHandler;
import com.mojang.serialization.DataResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FluidPipeBlockEntity extends CableBlockEntity<FluidPipeBlockEntity> implements IFluidHandlerProvider {
    protected final SingleFluidHandler singleFluidHandler;
    protected final int maxFluidTransfer;

    public FluidPipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int maxFluidTransfer) {
        super(type, pos, blockState);

        this.singleFluidHandler = new SingleFluidHandler(4000, maxFluidTransfer);
        this.maxFluidTransfer = maxFluidTransfer;
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull FluidPipeBlockEntity blockEntity) {
        if (level.isClientSide) return;

        List<FluidPipeBlockEntity> network = collectCableNetwork(level, pos);

        if (!network.isEmpty() && network.getFirst() == this) {
            equalizeFluid(network);

            for (FluidPipeBlockEntity cable : network) {
                cable.distributeToConsumers(level, cable.getBlockPos());
            }
        }
    }

    private List<FluidPipeBlockEntity> collectCableNetwork(Level level, BlockPos pos) {
        List<FluidPipeBlockEntity> cables = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        collectCablesRecursive(level, pos, cables, visited);
        return cables;
    }

    private void collectCablesRecursive(Level level, BlockPos pos, List<FluidPipeBlockEntity> cables, Set<BlockPos> visited) {
        if (!visited.add(pos)) return;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof FluidPipeBlockEntity cable) {
            cables.add(cable);
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.relative(direction);
                BlockEntity neighbor = level.getBlockEntity(neighborPos);
                if (neighbor instanceof FluidPipeBlockEntity) {
                    collectCablesRecursive(level, neighborPos, cables, visited);
                }
            }
        }
    }

    private void equalizeFluid(List<FluidPipeBlockEntity> cables) {
        if (cables.size() <= 1) return;

        long totalAmount = 0;
        long totalCapacity = 0;
        FluidStack fluidType = FluidStack.EMPTY;

        for (FluidPipeBlockEntity cable : cables) {
            FluidStack fs = cable.singleFluidHandler.getFluidInTank(0);
            if (!fs.isEmpty()) {
                if (fluidType.isEmpty()) fluidType = fs.copy();
                totalAmount += fs.getAmount();
            }
            totalCapacity += cable.singleFluidHandler.getTankCapacity(0);
        }

        if (fluidType.isEmpty() || totalAmount == 0) return;

        double fillRatio = (double) totalAmount / totalCapacity;

        long remainingAmount = totalAmount;
        for (int i = 0; i < cables.size(); i++) {
            FluidPipeBlockEntity cable = cables.get(i);
            int myCapacity = cable.singleFluidHandler.getTankCapacity(0);

            int targetAmount;
            if (i == cables.size() - 1) {
                targetAmount = (int) remainingAmount;
            } else {
                targetAmount = (int) Math.floor(myCapacity * fillRatio);
                remainingAmount -= targetAmount;
            }

            cable.singleFluidHandler.setFluid(new FluidStack(fluidType.getFluid(), targetAmount));
        }
    }

    private void distributeToConsumers(Level level, BlockPos pos) {
        FluidStack storedFluid = singleFluidHandler.getFluidInTank(0);
        if (storedFluid.isEmpty() || storedFluid.getAmount() <= 0) return;

        List<Consumer> consumers = getConsumers(level, pos);
        consumers.sort(Comparator.comparingDouble(c -> c.pos.distSqr(pos)));

        for (Consumer consumer : consumers) {
            if (consumer.canTransfer) {
                FluidStack toDistribute = new FluidStack(storedFluid.getFluid(), Math.min(storedFluid.getAmount(), maxFluidTransfer));
                int filled = consumer.handler.fill(toDistribute.copy(), FluidAction.EXECUTE);
                if (filled > 0) {
                    singleFluidHandler.drain(filled, FluidAction.EXECUTE);
                    VanillaTech.LOGGER.info("Transferred {} mB of {} from {} to {}", filled,
                            storedFluid.getFluid().getFluidType().getDescription(), pos, consumer.pos);
                    if (singleFluidHandler.getFluidInTank(0).getAmount() <= 0) break;
                }
            }
        }
    }

    private static class Consumer {
        BlockPos pos;
        IFluidHandler handler;
        boolean canTransfer;

        Consumer(BlockPos pos, IFluidHandler handler, boolean canTransfer) {
            this.pos = pos;
            this.handler = handler;
            this.canTransfer = canTransfer;
        }
    }

    private List<Consumer> getConsumers(Level level, BlockPos pos) {
        List<Consumer> consumers = new ArrayList<>();
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof FluidPipeBlock)) return consumers;

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            if (((FluidPipeBlock) state.getBlock()).canConnectTo(level, neighborPos, direction)) {
                IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, neighborPos, direction.getOpposite());
                if (handler != null && !(level.getBlockEntity(neighborPos) instanceof FluidPipeBlockEntity)) {
                    boolean canTransfer = true;
                    BlockEntity be = level.getBlockEntity(neighborPos);
                    if (be instanceof MachineBlockEntity machine) {
                        MachineInterface iface = machine.getInterface(direction.getOpposite(), neighborPos, level.getBlockState(neighborPos), MachineInterface.InterfaceType.FLUID);
                        if (iface != null) {
                            canTransfer = iface.ioType() == MachineInterface.InterfaceIO.IN || iface.ioType() == MachineInterface.InterfaceIO.BOTH;
                        } else {
                            canTransfer = false;
                        }
                    }
                    consumers.add(new Consumer(neighborPos, handler, canTransfer));
                }
            }
        }
        return consumers;
    }

    @Override
    public IFluidHandler getFluidHandler() {
        return singleFluidHandler;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        DataResult<Tag> result = SingleFluidHandler.CODEC.encodeStart(NbtOps.INSTANCE, singleFluidHandler);
        result.result().ifPresent(nbt -> {
            if (nbt instanceof CompoundTag compound) {
                tag.put("FluidHandler", compound);
            } else {
                VanillaTech.LOGGER.warn("Expected CompoundTag but got {}", nbt.getType().getName());
            }
        });
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("FluidHandler")) {
            DataResult<SingleFluidHandler> result = SingleFluidHandler.CODEC.parse(NbtOps.INSTANCE, tag.getCompound("FluidHandler"));
            result.result().ifPresent(handler -> {
                this.singleFluidHandler.setFluid(handler.getFluid());
            });
        }
    }
}
