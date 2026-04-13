package dev.shadowsoffire.apotheosis.mixin.ench.enchantment;

import dev.shadowsoffire.apotheosis.Apotheosis;
import dev.shadowsoffire.apotheosis.ench.EnchModule;
import dev.shadowsoffire.apotheosis.ench.Ench;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityDropMixin {

    @Shadow public abstract Level level();

    @Inject(method = "spawnAtLocation(Lnet/minecraft/world/item/ItemStack;F)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("HEAD"), cancellable = true)
    private void zenith$knowledgeOfTheAges(ItemStack stack, float yOffset, CallbackInfoReturnable<ItemEntity> cir) {
        if (stack.isEmpty() || this.level().isClientSide()) return;

        if ((Object) this instanceof LivingEntity target && !(target instanceof Player)) {

            if (target.isDeadOrDying() && target.getLastHurtByMob() instanceof Player p) {

                int knowledge = EnchantmentHelper.getItemEnchantmentLevel(Ench.Enchantments.KNOWLEDGE, p.getMainHandItem());

                if (knowledge > 0) {
                    if (Apotheosis.enableDebug) {
                        EnchModule.LOGGER.info("Item {} is being removed due to KOTA", stack.getItem());
                    }

                    int itemsValue = stack.getCount() * knowledge * 25;

                    while (itemsValue > 0) {
                        int xpValue = ExperienceOrb.getExperienceValue(itemsValue);
                        itemsValue -= xpValue;

                        p.level().addFreshEntity(new ExperienceOrb(
                                p.level(),
                                target.getX(),
                                target.getY(),
                                target.getZ(),
                                xpValue
                        ));
                    }

                    cir.setReturnValue(null);
                }
            }
        }
    }
}