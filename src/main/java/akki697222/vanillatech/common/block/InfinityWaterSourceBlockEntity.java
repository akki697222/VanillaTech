package akki697222.vanillatech.common.block;

import akki697222.vanillatech.api.common.IFluidHandlerProvider;
import akki697222.vanillatech.api.common.fluid.SingleFluidHandler;
import akki697222.vanillatech.common.VTBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class InfinityWaterSourceBlockEntity extends BlockEntity implements BlockEntityTicker<InfinityWaterSourceBlockEntity>, IFluidHandlerProvider {
    private final SingleFluidHandler singleFluidHandler;

    public InfinityWaterSourceBlockEntity(BlockPos pos, BlockState blockState) {
        super(VTBlockEntities.INFINITY_WATER_SOURCE.get(), pos, blockState);
        this.singleFluidHandler = new SingleFluidHandler(Integer.MAX_VALUE, Integer.MAX_VALUE);
        this.singleFluidHandler.fill(new FluidStack(Fluids.WATER, Integer.MAX_VALUE), IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public IFluidHandler getFluidHandler() {
        return singleFluidHandler;
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState blockState, @NotNull InfinityWaterSourceBlockEntity infinityWaterSourceBlockEntity) {
        for (Direction direction : Direction.values()) {
            IFluidHandler neighbor = level.getCapability(Capabilities.FluidHandler.BLOCK, pos.relative(direction), direction.getOpposite());
            if (neighbor != null) {
                neighbor.fill(new FluidStack(Fluids.WATER, singleFluidHandler.getFluid().getAmount()), IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }
}
