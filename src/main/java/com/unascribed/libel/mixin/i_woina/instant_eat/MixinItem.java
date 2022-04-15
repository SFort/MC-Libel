package com.unascribed.libel.mixin.i_woina.instant_eat;

import com.unascribed.libel.FabConf;
import net.minecraft.item.ItemFood;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.libel.support.EligibleIf;

import net.minecraft.item.ItemStack;

@Mixin(ItemFood.class)
@EligibleIf(configAvailable="*.instant_eat")
public class MixinItem {

	@Inject(method="getMaxItemUseDuration(Lnet/minecraft/item/ItemStack;)I", at=@At(value="HEAD"), cancellable = true)
	private void getMaxUseTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (FabConf.isEnabled("*.instant_eat")) cir.setReturnValue(1);
	}
}
