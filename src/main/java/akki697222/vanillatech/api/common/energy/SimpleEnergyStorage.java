package akki697222.vanillatech.api.common.energy;

import akki697222.vanillatech.api.common.fluid.SingleFluidHandler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;

public class SimpleEnergyStorage implements IEnergyStorage {
    public static final Codec<SimpleEnergyStorage> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("energyStored").forGetter(h -> h.energyStored),
                    Codec.INT.fieldOf("maxEnergy").forGetter(h -> h.maxEnergy),
                    Codec.INT.fieldOf("maxReceive").forGetter(h -> h.maxReceive),
                    Codec.INT.fieldOf("maxExtract").forGetter(h -> h.maxExtract)
            ).apply(instance, SimpleEnergyStorage::new)
    );

    private int energyStored;
    private int maxEnergy;
    private int maxReceive;
    private int maxExtract;

    public SimpleEnergyStorage(int maxEnergy, int maxReceive, int maxExtract) {
        this.maxEnergy = maxEnergy;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energyStored = 0;
    }

    public SimpleEnergyStorage(int maxEnergy, int maxReceive, int maxExtract, int energyStored) {
        this.maxEnergy = maxEnergy;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energyStored = energyStored;
    }

    /**
     * for syncing
     */
    @Deprecated
    public void updateMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    /**
     * for syncing
     */
    @Deprecated
    public void updateMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }

    /**
     * for syncing
     */
    @Deprecated
    public void updateEnergyStored(int energyStored) {
        this.energyStored = energyStored;
    }

    /**
     * for syncing
     */
    @Deprecated
    public void updateMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        if (!canReceive()) {
            return 0;
        }
        int energyReceived = Math.min(maxEnergy - energyStored, Math.min(maxReceive, amount));
        if (!simulate) {
            energyStored += energyReceived;
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        if (!canExtract()) {
            return 0;
        }
        int energyExtracted = Math.min(energyStored, Math.min(maxExtract, amount));
        if (!simulate) {
            energyStored -= energyExtracted;
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return energyStored;
    }

    @Override
    public int getMaxEnergyStored() {
        return maxEnergy;
    }

    @Override
    public boolean canExtract() {
        return energyStored > 0;
    }

    @Override
    public boolean canReceive() {
        return energyStored < maxEnergy;
    }

    public int getMaxReceive() {
        return maxReceive;
    }

    public int getMaxExtract() {
        return maxExtract;
    }
}
