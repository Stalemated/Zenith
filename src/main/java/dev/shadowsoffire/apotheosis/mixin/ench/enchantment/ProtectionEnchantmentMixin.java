package dev.shadowsoffire.apotheosis.mixin.ench.enchantment;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.shadowsoffire.apotheosis.Apotheosis;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProtectionEnchantment.class)
public abstract class ProtectionEnchantmentMixin {

    @Inject(method = "checkCompatibility(Lnet/minecraft/world/item/enchantment/Enchantment;)Z", at = @At("HEAD"), cancellable = true)
    protected void zenith$makeProtectionsCompatible(Enchantment other, CallbackInfoReturnable<Boolean> cir) {
        if (!Apotheosis.enableEnch) return;

        Enchantment thisEnchantment = (Enchantment) (Object) this;

        if (thisEnchantment == other) {
            cir.setReturnValue(false);
            return;
        }

        if (other instanceof ProtectionEnchantment) {
            cir.setReturnValue(true);
        }
    }

    @ModifyExpressionValue(method = "getDamageProtection", at = @At(value = "CONSTANT", args = "intValue=2", ordinal = 0))
    private int zenith$nerfFireProt(int originalValue) {
        return Apotheosis.enableEnch ? 1 : originalValue;
    }

    @ModifyExpressionValue(method = "getDamageProtection", at = @At(value = "CONSTANT", args = "intValue=2", ordinal = 2))
    private int zenith$nerfProjProt(int originalValue) {
        return Apotheosis.enableEnch ? 1 : originalValue;
    }
}