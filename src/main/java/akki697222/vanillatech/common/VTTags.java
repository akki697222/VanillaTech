package akki697222.vanillatech.common;

import akki697222.vanillatech.api.VanillaTechAPI;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class VTTags {
    public static <T> TagKey<T> optionalTag(Registry<T> registry, ResourceLocation id) {
        return TagKey.create(registry.key(), id);
    }

    public static <T> TagKey<T> commonTag(Registry<T> registry, String path) {
        return optionalTag(registry, ResourceLocation.fromNamespaceAndPath("c", path));
    }

    public static <T> TagKey<T> vtTag(Registry<T> registry, String path) {
        return optionalTag(registry, VanillaTechAPI.location(path));
    }

    public static TagKey<Block> commonBlockTag(String path) {
        return commonTag(BuiltInRegistries.BLOCK, path);
    }

    public static TagKey<Item> commonItemTag(String path) {
        return commonTag(BuiltInRegistries.ITEM, path);
    }

    public static TagKey<Fluid> commonFluidTag(String path) {
        return commonTag(BuiltInRegistries.FLUID, path);
    }

    public static TagKey<Block> vtBlockTag(String path) {
        return vtTag(BuiltInRegistries.BLOCK, path);
    }

    public static TagKey<Item> vtItemTag(String path) {
        return vtTag(BuiltInRegistries.ITEM, path);
    }

    public static TagKey<Fluid> vtFluidTag(String path) {
        return vtTag(BuiltInRegistries.FLUID, path);
    }

}
