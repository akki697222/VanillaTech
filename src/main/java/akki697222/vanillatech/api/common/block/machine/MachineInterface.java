package akki697222.vanillatech.api.common.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public record MachineInterface(BlockPos offset, InterfaceSide side, InterfaceType type, InterfaceIO ioType) {
    public enum InterfaceSide {
        FRONT,
        BACK,
        LEFT,
        RIGHT,
        UP,
        DOWN;

        public static Direction getFacingFromSide(Direction facing, InterfaceSide side) {
            return switch (side) {
                case FRONT -> facing;
                case BACK -> facing.getOpposite();
                case LEFT -> facing.getCounterClockWise();
                case RIGHT -> facing.getClockWise();
                case UP -> Direction.UP;
                case DOWN -> Direction.DOWN;
            };
        }
    }

    public enum InterfaceIO {
        IN,
        OUT,
        BOTH
    }

    public enum InterfaceType {
        ENERGY,
        FLUID,
        ITEM
    }
}
