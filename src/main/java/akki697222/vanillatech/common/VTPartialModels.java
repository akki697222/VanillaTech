package akki697222.vanillatech.common;

import akki697222.vanillatech.api.VanillaTechAPI;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VTPartialModels {
    public static final ModelResourceLocation ARC_FURNACE_ELECTRODE = ModelResourceLocation.standalone(VanillaTechAPI.location("block/arc_furnace_electrode"));
    public static final ModelResourceLocation ARC_FURNACE_ELECTRODE_ACTIVE = ModelResourceLocation.standalone(VanillaTechAPI.location("block/arc_furnace_electrode_active"));
}
