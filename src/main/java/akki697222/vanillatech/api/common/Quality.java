package akki697222.vanillatech.api.common;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum Quality implements StringRepresentable {
    BAD,
    NORMAL,
    GOOD;

    public static final Codec<Quality> CODEC = Codec.STRING.xmap(
            name -> Quality.valueOf(name.toUpperCase()),
            Quality::name
    );

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }
}
