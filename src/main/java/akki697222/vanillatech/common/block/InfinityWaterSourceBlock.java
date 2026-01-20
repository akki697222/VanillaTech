package akki697222.vanillatech.common.block;

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
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class InfinityWaterSourceBlock extends VTBlock implements EntityBlock, IBlockCapabilityProvider<IFluidHandler, Direction> {
    public InfinityWaterSourceBlock() {
        super(Properties.of());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new InfinityWaterSourceBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable IFluidHandler getCapability(Level level, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, Direction direction) {
        if (blockEntity instanceof InfinityWaterSourceBlockEntity infinityWaterSourceBlockEntity) {
            return infinityWaterSourceBlockEntity.getFluidHandler();
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : (lvl, pos, st, be) -> ((InfinityWaterSourceBlockEntity) be).tick(lvl, pos, st, (InfinityWaterSourceBlockEntity) be);
    }
}
