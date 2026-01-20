package akki697222.vanillatech.common;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.common.item.hammer.HammeringRecipe;
import akki697222.vanillatech.common.machine.arcfurnace.ArcFurnaceRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class VTRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, VanillaTech.MODID);

    public static final Supplier<RecipeType<ArcFurnaceRecipe>> ARC_FURNACE =
            RECIPE_TYPES.register("arc_furnace", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return VanillaTech.MODID + ":arc_furnace";
                }
            });

    public static final DeferredHolder<RecipeType<?>, RecipeType<HammeringRecipe>> HAMMERING =
            RECIPE_TYPES.register("hammering", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return VanillaTech.MODID + ":hammering";
                }
            });
}
