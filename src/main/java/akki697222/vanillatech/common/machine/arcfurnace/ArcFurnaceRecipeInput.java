package akki697222.vanillatech.common.machine.arcfurnace;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record ArcFurnaceRecipeInput(ItemStack input, ItemStack additive) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> input;
            case 1 -> additive;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
