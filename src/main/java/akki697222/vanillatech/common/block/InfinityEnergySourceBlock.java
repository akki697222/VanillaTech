package akki697222.vanillatech.common.block;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.common.block.VTBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InfinityEnergySourceBlock extends VTBlock implements EntityBlock, IBlockCapabilityProvider<IEnergyStorage, Direction> {
    public InfinityEnergySourceBlock() {
        super(Properties.of());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new InfinityEnergySourceBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable IEnergyStorage getCapability(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @Nullable BlockEntity blockEntity, Direction direction) {
        if (blockEntity instanceof InfinityEnergySourceBlockEntity infinityEnergySourceBlockEntity) {
            return infinityEnergySourceBlockEntity.getEnergyStorage();
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : (lvl, pos, st, be) -> ((InfinityEnergySourceBlockEntity) be).tick(lvl, pos, st, (InfinityEnergySourceBlockEntity) be);
    }
}
