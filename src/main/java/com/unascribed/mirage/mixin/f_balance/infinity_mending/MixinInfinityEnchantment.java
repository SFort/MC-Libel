package com.unascribed.mirage.mixin.f_balance.infinity_mending;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentArrowInfinite;
import net.minecraft.enchantment.EnchantmentMending;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentArrowInfinite.class)
@EligibleIf(configAvailable="*.infinity_mending")
public abstract class MixinInfinityEnchantment {

	@Inject(at=@At("HEAD"), method="canApplyTogether", cancellable=true)
	public void canAccept(Enchantment other, CallbackInfoReturnable<Boolean> ci) {
		if (FabConf.isEnabled("*.infinity_mending") && other instanceof EnchantmentMending) {
			ci.setReturnValue(true);
		}
	}

}
