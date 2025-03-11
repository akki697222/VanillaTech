package akki697222.vanillatech.common;

import akki697222.vanillatech.VanillaTech;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class VTCreativeTab {
    public static DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, VanillaTech.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> VANILLA_TECH_TAB = CREATIVE_MODE_TABS.register("vanillatech", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("misc.title." + VanillaTech.MODID))
                    .icon(() -> new ItemStack(VTItems.PRECISION_SILICON_WAFER.get()))
                    .displayItems((p, o) -> {
                        VTItems.itemList.forEach(i -> {
                            o.accept(i.get().getDefaultInstance());
                        });
                    }).build());
}
