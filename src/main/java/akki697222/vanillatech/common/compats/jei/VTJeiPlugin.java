package akki697222.vanillatech.common.compats.jei;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.VanillaTechAPI;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class VTJeiPlugin implements IModPlugin {
    public static final ResourceLocation ID = VanillaTechAPI.location("jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {

    }
}
