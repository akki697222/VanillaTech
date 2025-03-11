package akki697222.vanillatech.common.datagen;

import akki697222.vanillatech.api.common.block.machine.MachineBlock;
import akki697222.vanillatech.common.VTBlockEntities;
import akki697222.vanillatech.common.VTBlocks;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class VTBlockLootSubProvider extends BlockLootSubProvider {
    protected VTBlockLootSubProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        LootItemCondition.Builder isNotSlave = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(VTBlocks.ARC_FURNACE.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MachineBlock.SLAVE, false));
        add(VTBlocks.ARC_FURNACE.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(VTBlocks.ARC_FURNACE.get()))
                        .when(isNotSlave)
                )
        );
        dropSelf(VTBlocks.STEEL_BLOCK.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return VTBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
