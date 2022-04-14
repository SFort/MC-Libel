package com.unascribed.mirage.mixin.f_balance.food_always_edible;

import com.unascribed.mirage.FabConf;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.mirage.support.ConfigPredicates;
import com.unascribed.mirage.support.EligibleIf;

@Mixin(EntityPlayer.class)
@EligibleIf(configAvailable="*.food_always_edible")
public class MixinPlayerEntity {

	@Inject(method="canEat(Z)Z", at=@At("HEAD"), cancellable=true)
	public void canConsume(boolean ignoreHunger, CallbackInfoReturnable<Boolean> cir) {
		if (!FabConf.isEnabled("*.food_always_edible")) return;
		if (!ConfigPredicates.shouldRun("*.food_always_edible", (EntityPlayer)(Object)this)) return;
		cir.setReturnValue(true);
	}

}
