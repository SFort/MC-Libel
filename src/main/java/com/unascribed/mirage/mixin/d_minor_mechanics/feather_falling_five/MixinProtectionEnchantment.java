package com.unascribed.mirage.mixin.d_minor_mechanics.feather_falling_five;

import com.unascribed.mirage.FabConf;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.init.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.mirage.support.EligibleIf;

@Mixin(EnchantmentProtection.class)
@EligibleIf(configAvailable="*.feather_falling_five")
public class MixinProtectionEnchantment {

	@Inject(at=@At("RETURN"), method="getMaxLevel()I", cancellable=true)
	public void getMaxLevel(CallbackInfoReturnable<Integer> cir) {
		if (!FabConf.isEnabled("*.feather_falling_five")) return;
		if (((Object)this) == Enchantments.FEATHER_FALLING && cir.getReturnValueI() < 5) {
			cir.setReturnValue(5);
		}
	}

}
