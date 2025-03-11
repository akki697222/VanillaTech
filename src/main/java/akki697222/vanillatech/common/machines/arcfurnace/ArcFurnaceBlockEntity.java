package akki697222.vanillatech.common.machines.arcfurnace;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.common.Quality;
import akki697222.vanillatech.api.common.block.machine.MachineBlock;
import akki697222.vanillatech.api.common.block.machine.MachineEnergyInterface;
import akki697222.vanillatech.api.common.block.machine.MachineInterface;
import akki697222.vanillatech.api.common.energy.SimpleEnergyStorage;
import akki697222.vanillatech.common.VTBlockEntities;
import akki697222.vanillatech.common.VTComponents;
import akki697222.vanillatech.common.VTItems;
import akki697222.vanillatech.common.VTRecipeTypes;
import akki697222.vanillatech.api.common.block.machine.MachineBlockEntity;
import akki697222.vanillatech.common.item.QualityItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ArcFurnaceBlockEntity extends MachineBlockEntity {
    public static final int SLOT_ELECTRODE = 0;
    public static final int SLOT_INPUT_1 = 1;
    public static final int SLOT_INPUT_2 = 2;
    public static final int SLOT_ADDITIVE = 3;
    public static final int SLOT_SLAG = 4;
    public static final int SLOT_OUTPUT = 5;
    public static final int SLOT_ENERGY = 6;
    public static final int SLOT_NUM = 7;

    private int progress = 0;
    private int maxProgress = 0;

    private static final ItemStack SLAG_ITEM = new ItemStack(VTItems.SLAG);

    public ArcFurnaceBlockEntity(BlockPos pos, BlockState blockState) {
        super(
                VTBlockEntities.ARC_FURNACE.get(),
                pos,
                blockState,
                new SimpleEnergyStorage(32000, 1000, 0), 2);

        machineInterfaces.add(new MachineEnergyInterface(
                new BlockPos(0, 0, 1),
                MachineInterface.InterfaceSide.BACK));
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("gui." + VanillaTech.MODID + ".arc_furnace.title");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new ArcFurnaceMenu(containerId, inventory, this, this.containerData, this.simpleEnergyStorage);
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull MachineBlockEntity machineBlockEntity) {
        super.tick(level, blockPos, blockState, machineBlockEntity);
        if (level.isClientSide || blockState.getValue(MachineBlock.SLAVE)) return;

        ArcFurnaceRecipeInput input = new ArcFurnaceRecipeInput(processDoubleInput(getItem(SLOT_INPUT_1), getItem(SLOT_INPUT_2)), getItem(SLOT_ADDITIVE));

        Optional<RecipeHolder<ArcFurnaceRecipe>> recipeHolder = level.getRecipeManager().getRecipeFor(VTRecipeTypes.ARC_FURNACE.get(), input, level);

        boolean currentHasElectrode = blockState.getValue(ArcFurnaceBlock.HAS_ELECTRODE);
        boolean newHasElectrode = !getItem(SLOT_ELECTRODE).isEmpty();

        if (!blockState.getValue(MachineBlock.SLAVE)) {
            if (currentHasElectrode != newHasElectrode) {
                level.setBlock(blockPos, blockState.setValue(ArcFurnaceBlock.HAS_ELECTRODE, newHasElectrode), 2);
            }
        }

        if (recipeHolder.isPresent()) {
            ArcFurnaceRecipe recipe = recipeHolder.get().value();

            if (recipe.matches(input, level)
                    && !getItem(SLOT_ELECTRODE).isEmpty()) {
                ItemStack output = recipe.assemble(input, level.registryAccess());
                ItemStack slagOutput = SLAG_ITEM.copy();

                ItemStack currentOutput = inventory.getStackInSlot(SLOT_OUTPUT);
                ItemStack currentSlag = inventory.getStackInSlot(SLOT_SLAG);
                boolean canOutput = (currentOutput.isEmpty() || (currentOutput.is(output.getItem()) && currentOutput.getCount() + output.getCount() <= currentOutput.getMaxStackSize()))
                        && (currentSlag.isEmpty() || (currentSlag.is(slagOutput.getItem()) && currentSlag.getCount() + slagOutput.getCount() <= currentSlag.getMaxStackSize()));

                if (canOutput) {
                    int energyCostPerTick = recipe.getEnergyRequires() / recipe.getProcessingTime();
                    if (simpleEnergyStorage.getEnergyStored() >= energyCostPerTick) {
                        maxProgress = recipe.getProcessingTime();
                        progress++;
                        isActive = true;
                        simpleEnergyStorage.extractEnergy(energyCostPerTick, false);
                        setChanged();

                        if (progress >= recipe.getProcessingTime()) {
                            damageElectrode(getItem(SLOT_ELECTRODE));
                            if (!getItem(SLOT_INPUT_1).isEmpty()) {
                                inventory.extractItem(SLOT_INPUT_1, recipe.getInput().getCount(), false);
                            } else {
                                inventory.extractItem(SLOT_INPUT_2, recipe.getInput().getCount(), false);
                            }
                            inventory.extractItem(SLOT_ADDITIVE, 1, false);

                            if (currentOutput.isEmpty()) {
                                ItemStack tempOut = output.copy();
                                if (tempOut.getItem() instanceof QualityItem) {
                                    tempOut.set(VTComponents.QUALITY, Quality.NORMAL);
                                }
                                inventory.setStackInSlot(SLOT_OUTPUT, tempOut);
                            } else {
                                currentOutput.grow(output.getCount());
                            }

                            if (currentSlag.isEmpty()) {
                                inventory.setStackInSlot(SLOT_SLAG, slagOutput.copy());
                            } else {
                                currentSlag.grow(slagOutput.getCount());
                            }

                            progress = 0;
                            isActive = false;
                            setChanged();
                        }

                        containerData.set(2, progress);
                        containerData.set(3, maxProgress);
                    }
                }
            } else {
                progress = 0;
                isActive = false;
                containerData.set(2, progress);
                containerData.set(3, maxProgress);
            }
        } else {
            progress = 0;
            isActive = false;
            containerData.set(2, progress);
            containerData.set(3, maxProgress);
        }
    }

    private void damageElectrode(ItemStack electrode) {
        if (!electrode.isEmpty() && electrode.isDamageableItem()) {
            electrode.setDamageValue(electrode.getDamageValue() + 1);
            if (electrode.getDamageValue() >= electrode.getMaxDamage()) {
                electrode.shrink(1);
                if (electrode.isEmpty()) {
                    setItem(SLOT_ELECTRODE, ItemStack.EMPTY);
                }
            }
        }
    }

    public ItemStack processDoubleInput(ItemStack input1, ItemStack input2) {
        if (!input1.isEmpty()) {
            return input1;
        } else {
            return input2;
        }
    }
}
