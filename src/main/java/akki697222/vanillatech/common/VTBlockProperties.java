package akki697222.vanillatech.common;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class VTBlockProperties {
    public static final Properties MACHINE = Properties.of()
            .strength(3.5F, 6.0F)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)
            .noOcclusion();

    public static final Properties METAL = Properties.of()
            .mapColor(MapColor.METAL)
            .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
            .requiresCorrectToolForDrops()
            .strength(5.0F, 6.0F)
            .sound(SoundType.METAL);
}
