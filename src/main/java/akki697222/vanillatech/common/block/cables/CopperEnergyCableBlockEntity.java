package akki697222.vanillatech.common.block.cables;

import akki697222.vanillatech.common.VTBlockEntities;
import akki697222.vanillatech.common.block.EnergyCableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CopperEnergyCableBlockEntity extends EnergyCableBlockEntity {
    public CopperEnergyCableBlockEntity(BlockPos pos, BlockState blockState) {
        super(VTBlockEntities.COPPER_ENERGY_CABLE.get(), pos, blockState, 1000);
    }
}
