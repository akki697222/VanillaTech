package akki697222.vanillatech.common;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.common.block.InfinityEnergySourceBlockEntity;
import akki697222.vanillatech.common.block.InfinityWaterSourceBlockEntity;
import akki697222.vanillatech.common.block.cables.CopperEnergyCableBlockEntity;
import akki697222.vanillatech.common.block.cables.CopperFluidPipeBlockEntity;
import akki697222.vanillatech.common.block.cables.GoldEnergyCableBlockEntity;
import akki697222.vanillatech.common.machine.arcfurnace.ArcFurnaceBlockEntity;

import akki697222.vanillatech.common.machine.boiler.solidfuel.SolidFuelBoilerBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class VTBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, VanillaTech.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ArcFurnaceBlockEntity>> ARC_FURNACE = BLOCK_ENTITIES.register("arc_furnace",
            () -> BlockEntityType.Builder.of(
                            ArcFurnaceBlockEntity::new,
                            VTBlocks.ARC_FURNACE.get()
                    )
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SolidFuelBoilerBlockEntity>> SOLID_FUEL_BOILER = BLOCK_ENTITIES.register("solid_fuel_boiler",
            () -> BlockEntityType.Builder.of(
                            SolidFuelBoilerBlockEntity::new,
                            VTBlocks.SOLID_FUEL_BOILER.get()
                    )
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CopperEnergyCableBlockEntity>> COPPER_ENERGY_CABLE = BLOCK_ENTITIES.register("copper_energy_cable",
            () -> BlockEntityType.Builder.of(
                            CopperEnergyCableBlockEntity::new,
                            VTBlocks.COPPER_ENERGY_CABLE.get()
                    )
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GoldEnergyCableBlockEntity>> GOLD_ENERGY_CABLE = BLOCK_ENTITIES.register("gold_energy_cable",
            () -> BlockEntityType.Builder.of(
                            GoldEnergyCableBlockEntity::new,
                            VTBlocks.GOLD_ENERGY_CABLE.get()
                    )
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CopperFluidPipeBlockEntity>> COPPER_FLUID_PIPE = BLOCK_ENTITIES.register("copper_fluid_pipe",
            () -> BlockEntityType.Builder.of(
                            CopperFluidPipeBlockEntity::new,
                            VTBlocks.COPPER_FLUID_PIPE.get()
                    )
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfinityEnergySourceBlockEntity>> INFINITY_ENERGY_SOURCE = BLOCK_ENTITIES.register("infinity_energy_source",
            () -> BlockEntityType.Builder.of(
                            InfinityEnergySourceBlockEntity::new,
                            VTBlocks.INFINITY_ENERGY_SOURCE.get()
                    )
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfinityWaterSourceBlockEntity>> INFINITY_WATER_SOURCE = BLOCK_ENTITIES.register("infinity_water_source",
            () -> BlockEntityType.Builder.of(
                            InfinityWaterSourceBlockEntity::new,
                            VTBlocks.INFINITY_WATER_SOURCE.get()
                    )
                    .build(null));
}
