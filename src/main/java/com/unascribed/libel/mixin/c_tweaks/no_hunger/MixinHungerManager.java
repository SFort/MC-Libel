package com.unascribed.libel.mixin.c_tweaks.no_hunger;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.ConfigPredicates;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.FoodStats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodStats.class)
@EligibleIf(configAvailable="*.no_hunger")
public abstract class MixinHungerManager {

	@Shadow
	private float foodSaturationLevel;

	@Inject(at=@At("HEAD"), method="onUpdate(Lnet/minecraft/entity/player/EntityPlayer;)V", cancellable=true)
	public void update(EntityPlayer pe, CallbackInfo ci) {
		if (FabConf.isEnabled("*.no_hunger")) {
			if (ConfigPredicates.shouldRun("*.no_hunger", pe) && !pe.isPotionActive(MobEffects.HUNGER)) {
				ci.cancel();
			}
		}
	}
}
