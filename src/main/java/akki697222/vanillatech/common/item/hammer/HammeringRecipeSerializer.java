package akki697222.vanillatech.common.item.hammer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class HammeringRecipeSerializer implements RecipeSerializer<HammeringRecipe> {
    private static final MapCodec<HammeringRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(HammeringRecipe::getIngredient),
            ItemStack.STRICT_CODEC.fieldOf("result").forGetter(HammeringRecipe::getResultItem),
            Codec.INT.optionalFieldOf("cooldown", 10).forGetter(HammeringRecipe::getCooldown),
            Codec.INT.optionalFieldOf("durability_cost", 1).forGetter(HammeringRecipe::getDurabilityCost)
    ).apply(inst, HammeringRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, HammeringRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, HammeringRecipe::getIngredient,
            ItemStack.STREAM_CODEC, HammeringRecipe::getResultItem,
            ByteBufCodecs.VAR_INT, HammeringRecipe::getCooldown,
            ByteBufCodecs.VAR_INT, HammeringRecipe::getDurabilityCost,
            HammeringRecipe::new
    );

    @Override
    public @NotNull MapCodec<HammeringRecipe> codec() { return CODEC; }

    @Override
    public @NotNull StreamCodec<RegistryFriendlyByteBuf, HammeringRecipe> streamCodec() { return STREAM_CODEC; }
}