package akki697222.vanillatech.common;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.common.block.VTBlock;
import akki697222.vanillatech.common.block.InfinityEnergySourceBlock;
import akki697222.vanillatech.common.block.InfinityWaterSourceBlock;
import akki697222.vanillatech.common.block.cables.CopperEnergyCableBlock;
import akki697222.vanillatech.common.block.cables.CopperFluidPipeBlock;
import akki697222.vanillatech.common.block.cables.GoldEnergyCableBlock;
import akki697222.vanillatech.common.machine.arcfurnace.ArcFurnaceBlock;
import akki697222.vanillatech.common.machine.boiler.solidfuel.SolidFuelBoilerBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class VTBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(VanillaTech.MODID);

    public static final DeferredHolder<Block, ArcFurnaceBlock> ARC_FURNACE = registerBlock("arc_furnace", ArcFurnaceBlock::new);
    public static final DeferredHolder<Block, SolidFuelBoilerBlock> SOLID_FUEL_BOILER = registerBlock("solid_fuel_boiler", SolidFuelBoilerBlock::new);

    public static final DeferredHolder<Block, CopperEnergyCableBlock> COPPER_ENERGY_CABLE = registerBlock("copper_energy_cable", CopperEnergyCableBlock::new);
    public static final DeferredHolder<Block, GoldEnergyCableBlock> GOLD_ENERGY_CABLE = registerBlock("gold_energy_cable", GoldEnergyCableBlock::new);
    public static final DeferredHolder<Block, CopperFluidPipeBlock> COPPER_FLUID_PIPE = registerBlock("copper_fluid_pipe", CopperFluidPipeBlock::new);
    public static final DeferredHolder<Block, InfinityEnergySourceBlock> INFINITY_ENERGY_SOURCE = registerBlock("infinity_energy_source", InfinityEnergySourceBlock::new);
    public static final DeferredHolder<Block, InfinityWaterSourceBlock> INFINITY_WATER_SOURCE = registerBlock("infinity_water_source", InfinityWaterSourceBlock::new);

    // Metals
    public static final DeferredHolder<Block, VTBlock> STEEL_BLOCK = registerSimpleVTBlock("steel_block", () -> new VTBlock(VTBlockProperties.METAL));

    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block) {
        DeferredHolder<Block, T> blockHolder = BLOCKS.register(name, block);
        VTItems.registerBlockItem(blockHolder);
        return blockHolder;
    }

    private static <T extends VTBlock> DeferredHolder<Block, T> registerSimpleVTBlock(String name, Supplier<T> block) {
        DeferredHolder<Block, T> blockHolder = BLOCKS.register(name, block);
        VTItems.registerBlockItem(blockHolder);
        return blockHolder;
    }

    private static <T extends Block> DeferredHolder<Block, T> registerMachineBlock(String name, Supplier<T> block) {
        DeferredHolder<Block, T> blockHolder = BLOCKS.register(name, block);
        VTItems.registerBlockItem(blockHolder);
        return blockHolder;
    }
}
