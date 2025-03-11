package akki697222.vanillatech.common.datagen;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.common.VTItems;
import akki697222.vanillatech.common.VTTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class VTItemTagProvider extends ItemTagsProvider {
    public VTItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, VanillaTech.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(VTTags.commonItemTag("plates/iron")).add(VTItems.IRON_PLATE.get());
        tag(VTTags.commonItemTag("plates/gold")).add(VTItems.GOLD_PLATE.get());
        tag(VTTags.commonItemTag("plates/copper")).add(VTItems.COPPER_PLATE.get());
        tag(VTTags.commonItemTag("plates/steel")).add(VTItems.STEEL_PLATE.get());
        tag(VTTags.vtItemTag("silicon/material")).add(VTItems.SILICON_INGOT.get());
    }
}
