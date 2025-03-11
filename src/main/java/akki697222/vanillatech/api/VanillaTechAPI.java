package akki697222.vanillatech.api;

import akki697222.vanillatech.VanillaTech;
import net.minecraft.resources.ResourceLocation;

public class VanillaTechAPI {
    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(VanillaTech.MODID, path);
    }
}
