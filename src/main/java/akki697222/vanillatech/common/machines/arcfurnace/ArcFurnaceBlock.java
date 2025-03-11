package akki697222.vanillatech.common.machines.arcfurnace;

import akki697222.vanillatech.api.common.block.machine.MachineEnergyInterface;
import akki697222.vanillatech.api.common.block.machine.MachineInterface;
import akki697222.vanillatech.common.VTBlockEntities;
import akki697222.vanillatech.common.VTBlockProperties;
import akki697222.vanillatech.api.common.block.machine.MachineBlock;
import akki697222.vanillatech.api.common.block.machine.MachineBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ArcFurnaceBlock extends MachineBlock {
    public static MapCodec<ArcFurnaceBlock> CODEC = MapCodec.unit(ArcFurnaceBlock::new);
    private static final List<BlockPos> customSlaves = new ArrayList<>();
    public static final BooleanProperty HAS_ELECTRODE = BooleanProperty.create("has_electrode");

    public ArcFurnaceBlock() {
        super(VTBlockProperties.MACHINE,
                new BlockPos(3, 2, 2),
                new BlockPos(1, 0, 0));

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HAS_ELECTRODE, false));

        customSlaves.add(new BlockPos(1, 0, -1));
        customSlaves.add(new BlockPos(1, 2, 0));
        customSlaves.add(new BlockPos(1, 2, 1));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ArcFurnaceBlockEntity(blockPos, blockState);
    }

    @Override
    protected void placeSlaveBlocks(MachineBlockEntity master, Level level, BlockPos pos, Direction facing) {
        super.placeSlaveBlocks(master, level, pos, facing);

        customSlaves.forEach(blockPos -> placeSlaveBlock(master, level, pos, facing, blockPos.getX(), blockPos.getY(), blockPos.getZ()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HAS_ELECTRODE);
    }

    @Override
    protected boolean canPlace(Level level, BlockPos pos, Direction facing) {
        for (BlockPos blockPos : customSlaves) {
            if (!level.getBlockState(adjustPos(pos, offset, facing, blockPos.getX(), blockPos.getY(), blockPos.getZ())).canBeReplaced()) {
                return false;
            }
        }

        return super.canPlace(level, pos, facing);
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean moved) {
        for (BlockPos blockPos : customSlaves) {
            BlockPos slavePos = adjustPos(pos, offset, state.getValue(FACING), blockPos.getX(), blockPos.getY(), blockPos.getZ());
            BlockState slaveState = level.getBlockState(slavePos);
            if (slaveState.is(this) && slaveState.getValue(SLAVE)) {
                level.destroyBlock(slavePos, false);
            }
        }

        super.onRemove(state, level, pos, newState, moved);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if (blockEntityType == VTBlockEntities.ARC_FURNACE.get()) {
            return (lvl, pos, st, be) -> ((ArcFurnaceBlockEntity) be).tick(lvl, pos, st, (MachineBlockEntity) be);
        }
        return null;
    }

    @Override
    protected @NotNull MapCodec<ArcFurnaceBlock> codec() {
        return CODEC;
    }
}
