package akki697222.vanillatech.api.common.block.machine;

import akki697222.vanillatech.api.common.energy.SimpleEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class MachineEnergyInterface extends MachineInterface {
    public MachineEnergyInterface(BlockPos offset, InterfaceSide side) {
        super(offset, side);
    }
}
