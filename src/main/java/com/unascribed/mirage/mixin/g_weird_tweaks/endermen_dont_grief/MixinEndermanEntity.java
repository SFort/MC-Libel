package com.unascribed.mirage.mixin.g_weird_tweaks.endermen_dont_grief;

import com.unascribed.mirage.FabConf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.mirage.support.EligibleIf;

@Mixin(targets={"net.minecraft.entity.monster.EntityEnderman$AITakeBlock","net.minecraft.entity.monster.EntityEnderman$AIPlaceBlock"})
@EligibleIf(configAvailable="*.endermen_dont_grief")
public class MixinEndermanEntity {

	@Inject(method="shouldExecute()Z", at=@At("HEAD"), cancellable=true)
	private void canStart(CallbackInfoReturnable<Boolean> info) {
		if (FabConf.isEnabled("*.endermen_dont_grief")) {
			info.setReturnValue(false);
		}
	}
}
