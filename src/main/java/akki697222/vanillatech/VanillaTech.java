package akki697222.vanillatech;

import akki697222.vanillatech.api.common.IEnergyStorageProvider;
import akki697222.vanillatech.api.common.IFluidHandlerProvider;
import akki697222.vanillatech.common.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod(VanillaTech.MODID)
public class VanillaTech {
    public static final String MODID = "vanillatech";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public VanillaTech(IEventBus eventBus, ModContainer modContainer) {
        VTComponents.COMPONENTS.register(eventBus);
        VTItems.ITEMS.register(eventBus);
        VTBlocks.BLOCKS.register(eventBus);
        VTBlockEntities.BLOCK_ENTITIES.register(eventBus);
        VTFluidTypes.FLUID_TYPES.register(eventBus);
        VTFluids.FLUIDS.register(eventBus);
        VTCreativeTab.CREATIVE_MODE_TABS.register(eventBus);
        VTRecipeTypes.RECIPE_TYPES.register(eventBus);
        VTRecipeSerializers.RECIPE_SERIALIZERS.register(eventBus);
        VTMenuTypes.MENU_TYPES.register(eventBus);

        eventBus.addListener(this::registerCapabilities);
    }

    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        registerEnergyCapability(event, VTBlocks.INFINITY_ENERGY_SOURCE.get());
        registerFluidCapability(event, VTBlocks.INFINITY_WATER_SOURCE.get());
        registerEnergyCapability(event, VTBlocks.ARC_FURNACE.get());
        registerFluidCapability(event, VTBlocks.SOLID_FUEL_BOILER.get());
        registerEnergyCapability(event, VTBlocks.COPPER_ENERGY_CABLE.get());
        registerEnergyCapability(event, VTBlocks.GOLD_ENERGY_CABLE.get());
        registerFluidCapability(event, VTBlocks.COPPER_FLUID_PIPE.get());
    }

    private static void registerEnergyCapability(RegisterCapabilitiesEvent event, Block block) {
        event.registerBlock(
                Capabilities.EnergyStorage.BLOCK,
                VanillaTech::provideEnergyCapability,
                block
        );
        LOGGER.info("Registered energy capability for block {}", block);
    }

    private static void registerFluidCapability(RegisterCapabilitiesEvent event, Block block) {
        event.registerBlock(
                Capabilities.FluidHandler.BLOCK,
                VanillaTech::providerFluidCapability,
                block
        );
    }

    private static @Nullable IEnergyStorage provideEnergyCapability(
            Level level,
            BlockPos pos,
            BlockState state,
            @Nullable BlockEntity blockEntity,
            Direction direction
    ) {
        if (blockEntity instanceof IEnergyStorageProvider energyStorageProvider) {
            return energyStorageProvider.getEnergyStorage();
        }
        return null;
    }

    private static @Nullable IFluidHandler providerFluidCapability(
            Level level,
            BlockPos pos,
            BlockState state,
            @Nullable BlockEntity blockEntity,
            Direction direction
    ) {
        if (blockEntity instanceof IFluidHandlerProvider fluidHandlerProvider) {
            return fluidHandlerProvider.getFluidHandler();
        }
        return null;
    }
}
