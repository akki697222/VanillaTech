package akki697222.vanillatech.common.block;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.common.IEnergyStorageProvider;
import akki697222.vanillatech.api.common.block.cable.CableBlockEntity;
import akki697222.vanillatech.api.common.energy.SimpleEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class EnergyCableBlockEntity extends CableBlockEntity<EnergyCableBlockEntity> implements IEnergyStorageProvider {
    protected final int maxEnergyTransfer;
    protected final SimpleEnergyStorage simpleEnergyStorage;

    public EnergyCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int maxEnergyTransfer) {
        super(type, pos, blockState);

        this.maxEnergyTransfer = maxEnergyTransfer;
        this.simpleEnergyStorage = new SimpleEnergyStorage(4000, 4000, maxEnergyTransfer);
    }

    @Override
    public IEnergyStorage getEnergyStorage() {
        return simpleEnergyStorage;
    }

    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull EnergyCableBlockEntity blockEntity) {
        if (level.isClientSide) return;
        int currentEnergy = simpleEnergyStorage.getEnergyStored();

        if (currentEnergy <= 0) return;


    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);

        simpleEnergyStorage.receiveEnergy(tag.getInt("energy"), false);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putInt("energy", simpleEnergyStorage.getEnergyStored());
    }
}
