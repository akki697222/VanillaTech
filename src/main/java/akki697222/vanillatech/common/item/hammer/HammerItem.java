package akki697222.vanillatech.common.item.hammer;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.common.Quality;
import akki697222.vanillatech.common.VTComponents;
import akki697222.vanillatech.common.VTItems;
import akki697222.vanillatech.common.VTRecipeTypes;
import akki697222.vanillatech.common.VTTags;
import akki697222.vanillatech.common.item.VTItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HammerItem extends VTItem {
    public HammerItem() {
        super(new Properties().durability(576), "tooltip." + VanillaTech.MODID + ".desc.hammer");
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.pass(stack);
        }

        if (player.isSpectator()) {
            return InteractionResultHolder.pass(stack);
        }

        double range = 5.0;
        Vec3 start = player.getEyePosition();
        Vec3 look = player.getViewVector(1.0F);
        Vec3 end = start.add(look.scale(range));

        AABB boundingBox = new AABB(start, end).inflate(1.0);
        List<Entity> entities = level.getEntities(player, boundingBox, entity -> entity instanceof ItemEntity);

        for (Entity entity : entities) {
            if (entity instanceof ItemEntity itemEntity) {
                ItemStack droppedStack = itemEntity.getItem();

                var recipeHolder = level.getRecipeManager()
                        .getRecipeFor(VTRecipeTypes.HAMMERING.get(), new SingleRecipeInput(droppedStack), level)
                        .orElse(null);

                if (recipeHolder != null) {
                    HammeringRecipe recipe = recipeHolder.value();

                    ItemStack resultStack = recipe.getResultItem().copy();
                    resultStack.setCount(droppedStack.getCount());
                    resultStack.set(VTComponents.QUALITY.get(), Quality.BAD);

                    itemEntity.setItem(resultStack);

                    if (!player.isCreative()) {
                        stack.hurtAndBreak(recipe.getDurabilityCost() * droppedStack.getCount(), player, EquipmentSlot.MAINHAND);
                    }
                    player.getCooldowns().addCooldown(this, recipe.getCooldown());
                    player.playSound(SoundEvents.ANVIL_USE, 1.0F, 1.0F);

                    return InteractionResultHolder.success(stack);
                }
            }
        }

        return InteractionResultHolder.pass(stack);
    }
}
