package com.unascribed.mirage.mixin.f_balance.disable_elytra;

import com.unascribed.mirage.FabConf;
import net.minecraft.item.ItemElytra;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.mirage.support.ConfigPredicates;
import com.unascribed.mirage.support.EligibleIf;

import net.minecraft.item.ItemStack;

@Mixin(ItemElytra.class)
@EligibleIf(configAvailable="*.disable_elytra")
public class MixinElytraItem {

	@Inject(at=@At("HEAD"), method="isUsable(Lnet/minecraft/item/ItemStack;)Z", cancellable=true)
	private static void isUsable(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
		if (FabConf.isEnabled("*.disable_elytra") && ConfigPredicates.shouldRun("*.disable_elytra", stack)) {
			ci.setReturnValue(false);
		}
	}

}
