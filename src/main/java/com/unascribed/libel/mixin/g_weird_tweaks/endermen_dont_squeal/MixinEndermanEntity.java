package com.unascribed.libel.mixin.g_weird_tweaks.endermen_dont_squeal;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityEnderman.class)
@EligibleIf(configAvailable="*.endermen_dont_squeal")
public class MixinEndermanEntity {

	@Inject(at=@At("HEAD"), method="playEndermanSound()V", cancellable=true)
	public void playAngrySound(CallbackInfo ci) {
		if (!FabConf.isEnabled("*.endermen_dont_squeal")) return;
		ci.cancel();
	}

	@Inject(at=@At("HEAD"), method="getAmbientSound()Lnet/minecraft/util/SoundEvent;", cancellable=true)
	public void getAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
		if (!FabConf.isEnabled("*.endermen_dont_squeal")) return;
		cir.setReturnValue(SoundEvents.ENTITY_ENDERMEN_AMBIENT);
	}

}
