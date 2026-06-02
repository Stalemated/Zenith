package dev.shadowsoffire.apotheosis.mixin.compat.spell_power.present;

import dev.shadowsoffire.apotheosis.Apotheosis;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "net.spell_power.internals.MagicProtectionEnchantment")
public class MagicProtectionEnchantmentMixin {

    @Inject(method = "checkCompatibility(Lnet/minecraft/world/item/enchantment/Enchantment;)Z", at = @At("HEAD"), cancellable = true)
    private void zenith$makeMagicProtCompatible(Enchantment other, CallbackInfoReturnable<Boolean> cir) {
        if (!Apotheosis.enableEnch) return;

        if (other.getClass().getName().equals("net.spell_power.internals.MagicProtectionEnchantment")) {
            cir.setReturnValue(false);
            return;
        }

        if (other instanceof ProtectionEnchantment) {
            cir.setReturnValue(true);
        }
    }
}