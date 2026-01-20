package akki697222.vanillatech.common.compats.jei;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.VanillaTechAPI;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JeiPlugin
public class VTJeiPlugin implements IModPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(VanillaTech.MODID.concat("/JEI"));
    public static final ResourceLocation ID = VanillaTechAPI.location("jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {

    }
}
