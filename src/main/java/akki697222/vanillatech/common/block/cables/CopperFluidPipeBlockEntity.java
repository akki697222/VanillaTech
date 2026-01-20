package akki697222.vanillatech.common.block.cables;

import akki697222.vanillatech.common.VTBlockEntities;
import akki697222.vanillatech.common.block.FluidPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CopperFluidPipeBlockEntity extends FluidPipeBlockEntity {
    public CopperFluidPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(VTBlockEntities.COPPER_FLUID_PIPE.get(), pos, blockState, 2000);
    }
}
