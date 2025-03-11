package akki697222.vanillatech.common.item;

import akki697222.vanillatech.api.common.Quality;
import akki697222.vanillatech.common.VTComponents;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class QualityItem extends VTItem {
    public QualityItem(Properties properties, String tooltipTranslationKey) {
        super(properties, tooltipTranslationKey);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.set(VTComponents.QUALITY.get(), Quality.NORMAL);
        return stack;
    }
}
