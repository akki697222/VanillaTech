package akki697222.vanillatech.common.block;

import akki697222.vanillatech.api.common.IEnergyStorageProvider;
import akki697222.vanillatech.api.common.energy.SimpleEnergyStorage;
import akki697222.vanillatech.common.VTBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

public class InfinityEnergySourceBlockEntity extends BlockEntity implements BlockEntityTicker<InfinityEnergySourceBlockEntity>, IEnergyStorageProvider {
    private final SimpleEnergyStorage simpleEnergyStorage;

    public InfinityEnergySourceBlockEntity(BlockPos pos, BlockState blockState) {
        super(VTBlockEntities.INFINITY_ENERGY_SOURCE.get(), pos, blockState);
        this.simpleEnergyStorage = new SimpleEnergyStorage(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        this.simpleEnergyStorage.receiveEnergy(Integer.MAX_VALUE, false);
    }

    @Override
    public IEnergyStorage getEnergyStorage() {
        return simpleEnergyStorage;
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull InfinityEnergySourceBlockEntity infinityEnergySourceBlockEntity) {
        for (Direction direction : Direction.values()) {
            IEnergyStorage neighbor = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos.relative(direction), direction.getOpposite());
            if (neighbor != null && neighbor.canReceive()) {
                neighbor.receiveEnergy(simpleEnergyStorage.getEnergyStored(), false);
            }
        }
    }
}
