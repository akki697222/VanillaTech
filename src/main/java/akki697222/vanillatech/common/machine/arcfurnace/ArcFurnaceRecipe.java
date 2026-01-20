package akki697222.vanillatech.common.machine.arcfurnace;

import akki697222.vanillatech.api.common.recipe.MachineRecipe;
import akki697222.vanillatech.common.VTRecipeSerializers;
import akki697222.vanillatech.common.VTRecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ArcFurnaceRecipe extends MachineRecipe<ArcFurnaceRecipeInput> {
    private final ItemStack input;
    private final ItemStack output;
    private final ItemStack additive;
    private final int processingTime;
    private final int energyRequires;
    private final int outputSlags;

    public ArcFurnaceRecipe(ItemStack input, ItemStack output, ItemStack additive, int processingTime, int energyRequires, int outputSlags) {
        super(VTRecipeTypes.ARC_FURNACE.get());
        this.input = input;
        this.output = output;
        this.additive = additive;
        this.processingTime = processingTime;
        this.energyRequires = energyRequires;
        this.outputSlags = outputSlags;
    }

    @Override
    public boolean matches(ArcFurnaceRecipeInput recipeInput, @NotNull Level level) {
        boolean inputMatches = this.input.is(recipeInput.input().getItem())
                && recipeInput.input().getCount() >= this.input.getCount();
        boolean additiveMatches = this.additive.isEmpty() ||
                (this.additive.is(recipeInput.additive().getItem())
                        && recipeInput.additive().getCount() >= this.additive.getCount());

        return inputMatches && additiveMatches;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull ArcFurnaceRecipeInput recipeInput, HolderLookup.@NotNull Provider provider) {
        return output.copy();
    }

    public @NotNull ItemStack getResultItem() {
        return getResultItem(null);
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider provider) {
        return output.copy();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return VTRecipeSerializers.ARC_FURNACE.get();
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getAdditive() {
        return additive;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public int getEnergyRequires() {
        return energyRequires;
    }

    public int getOutputSlags() {
        return outputSlags;
    }
}
