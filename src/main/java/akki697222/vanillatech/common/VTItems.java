package akki697222.vanillatech.common;


import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.common.item.*;
import akki697222.vanillatech.common.item.hammer.HammerItem;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


public class VTItems {
    public static final List<DeferredHolder<Item, ? extends Item>> itemList = new ArrayList<>();
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(VanillaTech.MODID);

    public static final DeferredHolder<Item, SiliconWaferItem> SILICON_WAFER = registerItem("silicon_wafer", SiliconWaferItem::new);
    public static final DeferredHolder<Item, PrecisionSiliconWaferItem> PRECISION_SILICON_WAFER = registerItem("precision_silicon_wafer", PrecisionSiliconWaferItem::new);
    public static final DeferredHolder<Item, SiliconIngotItem> SILICON_INGOT = registerItem("silicon_ingot", SiliconIngotItem::new);
    public static final DeferredHolder<Item, HammerItem> HAMMER = registerItem("hammer", HammerItem::new);
    public static final DeferredHolder<Item, SlagItem> SLAG = registerItem("slag", SlagItem::new);
    public static final DeferredHolder<Item, ElectrodeItem> ELECTRODE = registerItem("electrode", ElectrodeItem::new);

    // Metals
    public static final DeferredHolder<Item, IronPlateItem> IRON_PLATE = registerItem("iron_plate", IronPlateItem::new);
    public static final DeferredHolder<Item, GoldPlateItem> GOLD_PLATE = registerItem("gold_plate", GoldPlateItem::new);
    public static final DeferredHolder<Item, CopperPlateItem> COPPER_PLATE = registerItem("copper_plate", CopperPlateItem::new);
    public static final DeferredHolder<Item, SteelPlateItem> STEEL_PLATE = registerItem("steel_plate", SteelPlateItem::new);
    public static final DeferredHolder<Item, SteelIngotItem> STEEL_INGOT = registerItem("steel_ingot", SteelIngotItem::new);

    public static <T extends Item> DeferredHolder<Item, T> registerItem(String name, Supplier<T> item) {
        DeferredHolder<Item, T> i = ITEMS.register(name, item);
        itemList.add(i);
        return i;
    }

    public static DeferredHolder<Item, BlockItem> registerBlockItem(DeferredHolder<Block, ? extends Block> blockHolder) {
        DeferredHolder<Item, BlockItem> i = ITEMS.registerSimpleBlockItem(blockHolder);
        itemList.add(i);
        return i;
    }
}
