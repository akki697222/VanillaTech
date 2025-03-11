package akki697222.vanillatech.common.item.hammer;

import akki697222.vanillatech.VanillaTech;
import akki697222.vanillatech.api.common.Quality;
import akki697222.vanillatech.common.VTComponents;
import akki697222.vanillatech.common.VTItems;
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
                AABB entityBox = itemEntity.getBoundingBox();
                var clipResult = entityBox.clip(start, end);
                if (clipResult.isPresent()) {
                    ItemStack droppedStack = itemEntity.getItem();
                    boolean success = false;
                    int usedDurability = droppedStack.getCount();

                    if (droppedStack.is(VTTags.commonItemTag("ingots/iron"))) {
                        ItemStack plateStack = new ItemStack(VTItems.IRON_PLATE.get(), droppedStack.getCount());
                        plateStack.set(VTComponents.QUALITY.get(), Quality.BAD);
                        itemEntity.setItem(plateStack);

                        player.getCooldowns().addCooldown(this, 10);
                        success = true;
                    } else if (droppedStack.is(VTTags.commonItemTag("ingots/gold"))) {
                        ItemStack plateStack = new ItemStack(VTItems.GOLD_PLATE.get(), droppedStack.getCount());
                        plateStack.set(VTComponents.QUALITY.get(), Quality.BAD);
                        itemEntity.setItem(plateStack);

                        player.getCooldowns().addCooldown(this, 10);
                        success = true;
                    } else if (droppedStack.is(VTTags.commonItemTag("ingots/copper"))) {
                        ItemStack plateStack = new ItemStack(VTItems.COPPER_PLATE.get(), droppedStack.getCount());
                        plateStack.set(VTComponents.QUALITY.get(), Quality.BAD);
                        itemEntity.setItem(plateStack);

                        player.getCooldowns().addCooldown(this, 5);
                        success = true;
                    } else if (droppedStack.is(VTTags.commonItemTag("ingots/steel"))) {
                        ItemStack plateStack = new ItemStack(VTItems.STEEL_PLATE.get(), droppedStack.getCount());
                        plateStack.set(VTComponents.QUALITY.get(), Quality.BAD);
                        itemEntity.setItem(plateStack);

                        player.getCooldowns().addCooldown(this, 5);
                        success = true;
                    } else if (droppedStack.is(VTTags.vtItemTag("silicon/material"))) {
                        ItemStack plateStack = new ItemStack(VTItems.SILICON_WAFER.get(), droppedStack.getCount());
                        plateStack.set(VTComponents.QUALITY.get(), Quality.BAD);
                        itemEntity.setItem(plateStack);

                        player.getCooldowns().addCooldown(this, 20);
                        success = true;
                    }

                    if (success) {
                        if (!player.isCreative()) {
                            stack.hurtAndBreak(usedDurability, player, EquipmentSlot.MAINHAND);
                        }
                        player.playSound(SoundEvents.ANVIL_USE, 1.0F, 1.0F);
                        return InteractionResultHolder.success(stack);
                    }
                }
            }
        }

        return InteractionResultHolder.pass(stack);
    }
}
