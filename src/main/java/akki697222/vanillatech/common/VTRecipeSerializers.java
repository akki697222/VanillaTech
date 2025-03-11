package akki697222.vanillatech.common;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.common.machines.arcfurnace.ArcFurnaceRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class VTRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, VanillaTech.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, ArcFurnaceRecipeSerializer> ARC_FURNACE = RECIPE_SERIALIZERS.register("arc_furnace", ArcFurnaceRecipeSerializer::new);
}
