package akki697222.vanillatech.common;

import akki697222.vanillatech.VanillaTech;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class VTFluidTypes {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, VanillaTech.MODID);

    public static final DeferredHolder<FluidType, FluidType> STEAM_TYPE = FLUID_TYPES.register("steam",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("fluid.vanillatech.steam")
                    .density(-1000)
                    .viscosity(500)
                    .temperature(373)
                    .canPushEntity(false)
                    .canSwim(false)
            ){
                @Override
                @SuppressWarnings("removal")
                public void initializeClient(java.util.function.@NotNull Consumer<net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions() {
                        private static final ResourceLocation WATER_STILL = ResourceLocation.withDefaultNamespace("block/water_still");
                        private static final ResourceLocation WATER_FLOW = ResourceLocation.withDefaultNamespace("block/water_flow");

                        @Override
                        public @NotNull ResourceLocation getStillTexture() {
                            return WATER_STILL;
                        }

                        @Override
                        public @NotNull ResourceLocation getFlowingTexture() {
                            return WATER_FLOW;
                        }

                        @Override
                        public int getTintColor() {
                            return 0xAFFFFFFF;
                        }
                    });
                }
            });
}