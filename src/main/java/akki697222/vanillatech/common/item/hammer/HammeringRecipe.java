package akki697222.vanillatech.common.item.hammer;

import akki697222.vanillatech.common.VTRecipeSerializers;
import akki697222.vanillatech.common.VTRecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class HammeringRecipe implements Recipe<RecipeInput> {
    private final Ingredient ingredient;
    private final ItemStack result;
    private final int cooldown;
    private final int durabilityCost;

    public HammeringRecipe(Ingredient ingredient, ItemStack result, int cooldown, int durabilityCost) {
        this.ingredient = ingredient;
        this.result = result;
        this.cooldown = cooldown;
        this.durabilityCost = durabilityCost;
    }

    @Override
    public boolean matches(RecipeInput input, @NotNull Level level) {
        return this.ingredient.test(input.getItem(0));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeInput input, HolderLookup.@NotNull Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider provider) {
        return this.result;
    }

    public Ingredient getIngredient() { return ingredient; }
    public int getCooldown() { return cooldown; }
    public int getDurabilityCost() { return durabilityCost; }
    public ItemStack getResultItem() { return result; }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return VTRecipeSerializers.HAMMERING.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return VTRecipeTypes.HAMMERING.get();
    }
}