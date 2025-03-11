package akki697222.vanillatech.api.common.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public abstract class MachineInterface {
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

    protected final BlockPos offset;
    protected final InterfaceSide side;

    public MachineInterface(BlockPos offset, InterfaceSide side) {
        this.offset = offset;
        this.side = side;
    }

    public BlockPos getOffset() {
        return offset;
    }

    public InterfaceSide getSide() {
        return side;
    }
}
