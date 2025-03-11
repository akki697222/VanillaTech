package akki697222.vanillatech.api.common.block;

import akki697222.vanillatech.api.common.Quality;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class QualityBlock extends Block {
    public static final EnumProperty<Quality> QUALITY = EnumProperty.create("quality", Quality.class);

    public QualityBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(QUALITY, Quality.NORMAL));
    }
}
