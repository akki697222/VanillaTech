package akki697222.vanillatech.common;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.common.machine.arcfurnace.ArcFurnaceMenu;
import akki697222.vanillatech.common.machine.boiler.solidfuel.SolidFuelBoilerMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class VTMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, VanillaTech.MODID);

    public static final Supplier<MenuType<ArcFurnaceMenu>> ARC_FURNACE = MENU_TYPES.register("arc_furnace", () -> new MenuType<>(ArcFurnaceMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final Supplier<MenuType<SolidFuelBoilerMenu>> SOLID_FUEL_BOILER = MENU_TYPES.register("solid_fuel_boiler", () -> new MenuType<>(SolidFuelBoilerMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
