package akki697222.vanillatech.common;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.common.Quality;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class VTComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, VanillaTech.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Quality>> QUALITY =
            COMPONENTS.register("quality", () ->
                    DataComponentType.<Quality>builder()
                            .persistent(Quality.CODEC)
                            .build());
}
