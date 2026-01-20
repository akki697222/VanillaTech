package akki697222.vanillatech.common.machine.arcfurnace;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class ArcFurnaceRecipeSerializer implements RecipeSerializer<ArcFurnaceRecipe> {
    private static final MapCodec<ArcFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("input").forGetter(ArcFurnaceRecipe::getInput),
            ItemStack.CODEC.fieldOf("output").forGetter(recipe -> recipe.getResultItem(null)),
            ItemStack.CODEC.optionalFieldOf("additive", ItemStack.EMPTY).forGetter(ArcFurnaceRecipe::getAdditive),
            Codec.INT.optionalFieldOf("processing_time", 200).forGetter(ArcFurnaceRecipe::getProcessingTime),
            Codec.INT.optionalFieldOf("energy_requires", 1000).forGetter(ArcFurnaceRecipe::getEnergyRequires),
            Codec.INT.optionalFieldOf("slag_count", 1).forGetter(ArcFurnaceRecipe::getOutputSlags)
    ).apply(instance, ArcFurnaceRecipe::new));

    @Override
    public @NotNull MapCodec<ArcFurnaceRecipe> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, ArcFurnaceRecipe> streamCodec() {
        return StreamCodec.of(
                (buf, recipe) -> {
                    ItemStack.STREAM_CODEC.encode(buf, recipe.getInput());
                    ItemStack.STREAM_CODEC.encode(buf, recipe.getResultItem(null));
                    ItemStack.STREAM_CODEC.encode(buf, recipe.getAdditive());
                    buf.writeInt(recipe.getProcessingTime());
                    buf.writeInt(recipe.getEnergyRequires());
                    buf.writeInt(recipe.getOutputSlags());
                },
                buf -> new ArcFurnaceRecipe(
                        ItemStack.STREAM_CODEC.decode(buf),
                        ItemStack.STREAM_CODEC.decode(buf),
                        ItemStack.STREAM_CODEC.decode(buf),
                        buf.readInt(),
                        buf.readInt(),
                        buf.readInt()
                )
        );
    }
}