package akki697222.vanillatech.common.item;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.common.Quality;
import akki697222.vanillatech.common.VTComponents;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VTItem extends Item {
    private final String tooltipTranslationKey;

    public VTItem(Properties properties, String tooltipTranslationKey) {
        super(properties);
        this.tooltipTranslationKey = tooltipTranslationKey;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        Quality quality = stack.get(VTComponents.QUALITY.get());
        if (quality != null) {
            tooltipComponents.add(Component.translatable("tooltip.vanillatech.quality").append(Component.translatable("tooltip.vanillatech.quality." + quality.toString().toLowerCase() + (Screen.hasControlDown() ? ".alt" : ""))));
        }

        if (!tooltipTranslationKey.isEmpty()) {
            tooltipComponents.add(Component.translatable(Screen.hasShiftDown() ? tooltipTranslationKey : "tooltip." + VanillaTech.MODID + ".tips.shift_to_description"));
        }
    }
}
