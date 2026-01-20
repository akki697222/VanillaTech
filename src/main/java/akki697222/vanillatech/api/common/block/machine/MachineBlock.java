package akki697222.vanillatech.api.common.block.machine;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.common.Quality;
import akki697222.vanillatech.api.common.block.QualityHorizontalDirectionalBlock;
import akki697222.vanillatech.api.common.energy.SimpleEnergyStorage;
import akki697222.vanillatech.api.common.fluid.SingleFluidHandler;
import akki697222.vanillatech.common.VTComponents;
import akki697222.vanillatech.common.item.QualityItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class MachineBlock extends QualityHorizontalDirectionalBlock implements EntityBlock, IBlockCapabilityProvider<IEnergyStorage, Direction> {
    protected final BlockPos size;
    protected final BlockPos offset;
    public static final BooleanProperty SLAVE = BooleanProperty.create("slave");
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public MachineBlock(Properties properties, BlockPos size, BlockPos offset) {
        super(properties.noOcclusion());
        this.size = size;
        this.offset = offset;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(SLAVE, false)
                .setValue(ACTIVE, false));
    }

    public MachineBlock(Properties properties) {
        this(properties, new BlockPos(1, 1, 1), new BlockPos(0, 0, 0));
    }

    @Override
    public @Nullable IEnergyStorage getCapability(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @Nullable BlockEntity blockEntity, Direction side) {
        VanillaTech.LOGGER.info("Machine getCapability called");
        if (blockEntity instanceof MachineBlockEntity machineBlockEntity) {
            List<MachineInterface> machineInterfaces = machineBlockEntity.getMachineInterfaces();
            BlockPos masterPos = machineBlockEntity.getMasterPos();
            for (MachineInterface machineInterface : machineInterfaces) {
                VanillaTech.LOGGER.info("Checking Machine interface (Side: {}, Offset: {})", machineInterface.side(), machineInterface.offset());
                boolean isThis = masterPos != null && masterPos.offset(machineInterface.offset()).equals(blockPos);

                if (masterPos != null) {
                    VanillaTech.LOGGER.info(masterPos.offset(machineInterface.offset()).toString());
                }

                if (isThis) {
                    Direction direction = MachineInterface.InterfaceSide.getFacingFromSide(
                            blockState.getValue(MachineBlock.FACING),
                            machineInterface.side());

                    if (direction.equals(side)) {
                        return machineBlockEntity.getEnergyStorage();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public @Nullable PushReaction getPistonPushReaction(@NotNull BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SLAVE, QUALITY, ACTIVE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        Quality quality = null;
        if (stack.getItem() instanceof QualityItem && stack.has(VTComponents.QUALITY)) {
            quality = stack.get(VTComponents.QUALITY);
        }
        if (quality == null) {
            quality = Quality.NORMAL;
        }
        if (canPlace(level, pos, facing)) {
            return this.defaultBlockState().setValue(FACING, facing).setValue(SLAVE, false).setValue(ACTIVE, false).setValue(QUALITY, quality);
        }
        return null;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(
            @NotNull BlockState state,
            Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MenuProvider menuProvider) {
                player.openMenu(menuProvider);
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.SUCCESS;
    }

    protected boolean canPlace(Level level, BlockPos pos, Direction facing) {
        for (int x = 0; x < size.getX(); x++) {
            for (int y = 0; y < size.getY(); y++) {
                for (int z = 0; z < size.getZ(); z++) {
                    BlockPos offsetPos = adjustPos(pos, offset, facing, x, y, z);
                    if (!level.getBlockState(offsetPos).canBeReplaced()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    protected boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return true;
    }

    @Override
    protected boolean skipRendering(@NotNull BlockState state, @NotNull BlockState adjacentState, @NotNull Direction direction) {
        return state.getValue(SLAVE);
    }

    @Override
    public int getLightBlock(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return 0;
    }

    @Override
    protected void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        if (!state.getValue(SLAVE)) {
            placeSlaveBlocks((MachineBlockEntity) level.getBlockEntity(pos), level, pos, state.getValue(FACING));
        }
    }

    protected void placeSlaveBlocks(MachineBlockEntity master, Level level, BlockPos pos, Direction facing) {
        for (int x = 0; x < size.getX(); x++) {
            for (int y = 0; y < size.getY(); y++) {
                for (int z = 0; z < size.getZ(); z++) {
                    placeSlaveBlock(master, level, pos, facing, x, y, z);
                }
            }
        }
    }

    protected void placeSlaveBlock(MachineBlockEntity master, Level level, BlockPos pos, Direction facing, int x, int y, int z) {
        BlockPos slavePos = adjustPos(pos, offset, facing, x, y, z);
        BlockState machineState = level.getBlockState(pos);
        BlockState slaveState = this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(SLAVE, true)
                .setValue(ACTIVE, false)
                .setValue(QUALITY, Quality.NORMAL);
        if (!slavePos.equals(pos)) {
            level.setBlock(slavePos, slaveState, 3);
        }
        BlockEntity be = level.getBlockEntity(slavePos);
        if (be instanceof MachineBlockEntity machine) {
            machine.setMasterPos(pos);
            machine.energyStorage = master.getEnergyStorage();
            machine.fluidHandler = master.getFluidHandler();
            machine.containerData = master.getContainerData();
            machine.inventory = master.getInventory();
        }
    }

    @Override
    public @NotNull BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        if (state.getValue(SLAVE)) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MachineBlockEntity machineBlockEntity) {
                if (machineBlockEntity.getMasterPos() != null) {
                    level.destroyBlock(machineBlockEntity.getMasterPos(), true);
                } else {
                    VanillaTech.LOGGER.error("Unexpected Error: getMasterPos returned null");
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock()) && !state.getValue(SLAVE)) {
            Direction facing = state.getValue(FACING);
            for (int x = 0; x < size.getX(); x++) {
                for (int y = 0; y < size.getY(); y++) {
                    for (int z = 0; z < size.getZ(); z++) {
                        BlockPos slavePos = adjustPos(pos, offset, facing, x, y, z);
                        BlockState slaveState = level.getBlockState(slavePos);
                        if (slaveState.is(this) && slaveState.getValue(SLAVE)) {
                            level.destroyBlock(slavePos, false);
                        }
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, moved);
    }

    public static BlockPos adjustPos(BlockPos pos, BlockPos offset, Direction facing, int x, int y, int z) {
        return switch (facing) {
            case EAST -> pos.offset(-z + offset.getZ(), y, x - offset.getX());
            case SOUTH -> pos.offset(-x + offset.getX(), y, -z + offset.getZ());
            case WEST -> pos.offset(z - offset.getZ(), y, -x + offset.getX());
            default -> pos.offset(x - offset.getX(), y, z - offset.getZ()); // NORTH
        };
    }

    @Override
    public abstract @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState);

    @Override
    public abstract @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType);
}
