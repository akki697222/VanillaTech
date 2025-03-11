package akki697222.vanillatech.common.item;

import akki697222.vanillatech.VanillaTech;

public class ElectrodeItem extends QualityItem {
    public ElectrodeItem() {
        super(new Properties().durability(1152), "tooltip." + VanillaTech.MODID + ".desc.electrode");
    }
}
