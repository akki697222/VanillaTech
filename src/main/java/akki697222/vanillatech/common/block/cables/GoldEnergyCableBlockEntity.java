package akki697222.vanillatech.common.block.cables;

import akki697222.vanillatech.common.VTBlockEntities;
import akki697222.vanillatech.common.block.EnergyCableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GoldEnergyCableBlockEntity extends EnergyCableBlockEntity {
    public GoldEnergyCableBlockEntity(BlockPos pos, BlockState blockState) {
        super(VTBlockEntities.GOLD_ENERGY_CABLE.get(), pos, blockState, 2000);
    }
}
