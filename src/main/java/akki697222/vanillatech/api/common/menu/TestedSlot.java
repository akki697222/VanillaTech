package akki697222.vanillatech.api.common.menu;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class TestedSlot extends Slot {
    private final Predicate<ItemStack> cond;

    public TestedSlot(Container container, int slot, int x, int y, Predicate<ItemStack> cond) {
        super(container, slot, x, y);
        this.cond = cond;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return cond.test(stack);
    }
}
