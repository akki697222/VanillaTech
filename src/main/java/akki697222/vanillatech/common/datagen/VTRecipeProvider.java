package akki697222.vanillatech.common.datagen;

import akki697222.vanillatech.api.VanillaTechAPI;
import akki697222.vanillatech.common.VTBlocks;
import akki697222.vanillatech.common.VTItems;
import akki697222.vanillatech.common.machines.arcfurnace.ArcFurnaceRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class VTRecipeProvider extends RecipeProvider {
    public VTRecipeProvider(DataGenerator generator, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(generator.getPackOutput(), lookupProvider);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        arcFurnaceRecipe(
                new ItemStack(Items.IRON_INGOT, 1),
                new ItemStack(VTItems.STEEL_INGOT),
                new ItemStack(Items.COAL, 1),
                100,
                1000,
                1
        ).save(output, VanillaTechAPI.location("steel_ingot"));
        arcFurnaceRecipe(
                new ItemStack(Items.IRON_BLOCK, 1),
                new ItemStack(VTBlocks.STEEL_BLOCK.get(), 1),
                new ItemStack(Items.COAL_BLOCK, 1),
                800,
                9000,
                9
        ).save(output, VanillaTechAPI.location("steel_block"));
        arcFurnaceRecipe(
                new ItemStack(Items.QUARTZ, 4),
                new ItemStack(VTItems.SILICON_INGOT),
                new ItemStack(Items.COAL, 1),
                200,
                3000,
                2
        ).save(output, VanillaTechAPI.location("silicon_ingot"));
    }

    private static RecipeBuilder arcFurnaceRecipe(ItemStack input, ItemStack output, ItemStack additive, int processingTime, int energyRequires, int slagCount) {
        return new RecipeBuilder() {
            @Override
            public @NotNull RecipeBuilder unlockedBy(@NotNull String criterion, @NotNull Criterion<?> criterionTrigger) {
                return this;
            }

            @Override
            public @NotNull RecipeBuilder group(String group) {
                return this;
            }

            @Override
            public @NotNull Item getResult() {
                return output.getItem();
            }

            @Override
            public void save(@NotNull RecipeOutput recipeOutput, @NotNull ResourceLocation id) {
                ArcFurnaceRecipe recipe = new ArcFurnaceRecipe(input, output, additive, processingTime, energyRequires, slagCount);
                recipeOutput.accept(id, recipe, null);
            }
        };
    }
}