package akki697222.vanillatech.api.common.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public abstract class MachineRecipe<T extends RecipeInput> implements Recipe<T> {
    protected final RecipeType<?> type;

    protected MachineRecipe(@Nonnull RecipeType<?> type) {
        this.type = type;
    }

    @Override
    public final boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return type;
    }
}
