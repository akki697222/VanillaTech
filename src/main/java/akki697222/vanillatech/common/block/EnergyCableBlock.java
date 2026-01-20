package akki697222.vanillatech.common.block;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.common.block.cable.CableBlock;
import akki697222.vanillatech.api.common.block.machine.MachineBlock;
import akki697222.vanillatech.api.common.block.machine.MachineBlockEntity;
import akki697222.vanillatech.api.common.block.machine.MachineInterface;
import akki697222.vanillatech.api.common.energy.SimpleEnergyStorage;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class EnergyCableBlock extends CableBlock implements IBlockCapabilityProvider<IEnergyStorage, Direction> {
    @Override
    public boolean canConnectTo(LevelAccessor levelAccessor, BlockPos pos, Direction direction) {
        if (!(levelAccessor instanceof Level level)) {
            VanillaTech.LOGGER.warn("LevelAccessor at {} is not an instance of Level, skipping capability check", pos);
            return false;
        }

        boolean canConnect = level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction.getOpposite()) != null;

        if (level.getBlockEntity(pos) instanceof MachineBlockEntity machineBlockEntity) {
            canConnect = machineBlockEntity.getPort(direction.getOpposite(), pos, level.getBlockState(pos), MachineInterface.InterfaceType.ENERGY) != null;
        }

        return canConnect;
    }

    @Override
    public @Nullable IEnergyStorage getCapability(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @Nullable BlockEntity blockEntity, Direction direction) {
        if (blockEntity instanceof EnergyCableBlockEntity energyCableBlockEntity) {
            return energyCableBlockEntity.getEnergyStorage();
        }
        return null;
    }

    @Override
    public abstract @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState);

    @Override
    public abstract @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType);

    @Override
    public abstract @NotNull MapCodec<? extends DirectionalBlock> codec();
}
