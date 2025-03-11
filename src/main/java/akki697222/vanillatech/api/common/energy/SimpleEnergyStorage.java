package akki697222.vanillatech.api.common.energy;

import net.neoforged.neoforge.energy.IEnergyStorage;

public class SimpleEnergyStorage implements IEnergyStorage {
    private int energyStored;
    private int maxEnergy;
    private final int maxReceive;
    private final int maxExtract;

    public SimpleEnergyStorage(int maxEnergy, int maxReceive, int maxExtract) {
        this.maxEnergy = maxEnergy;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energyStored = 0;
    }

    @Deprecated
    public void updateEnergyStored(int energyStored) {
        this.energyStored = energyStored;
    }

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
}
