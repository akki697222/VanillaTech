package akki697222.vanillatech.common.machine.boiler.solidfuel;

import akki697222.vanillatech.api.common.block.machine.MachineBlock;
import akki697222.vanillatech.common.VTBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SolidFuelBoilerBlock extends MachineBlock {
    public SolidFuelBoilerBlock() {
        super(VTBlockProperties.MACHINE,
                new BlockPos(3, 3, 2),
                new BlockPos(1, 0, 0));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new SolidFuelBoilerBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : (lvl, pos, st, be) -> ((SolidFuelBoilerBlockEntity) be).tick(lvl, pos, st, (SolidFuelBoilerBlockEntity) be);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
