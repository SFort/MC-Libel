package com.unascribed.mirage.mixin.f_balance.disable_prior_work_penalty;

import com.unascribed.mirage.FabConf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.mirage.support.EligibleIf;

import net.minecraft.item.ItemStack;

@Mixin(ItemStack.class)
@EligibleIf(anyConfigAvailable={"*.disable_prior_work_penalty", "*.anvil_no_xp_cost"})
public class MixinItemStack {

	@Inject(at=@At("HEAD"), method="getRepairCost()I", cancellable=true)
	public void getRepairCost(CallbackInfoReturnable<Integer> cir) {
		if (!(FabConf.isEnabled("*.disable_prior_work_penalty") || FabConf.isEnabled("*.anvil_no_xp_cost"))) return;
		cir.setReturnValue(0);
	}

}