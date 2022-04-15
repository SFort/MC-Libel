package com.unascribed.mirage.mixin.h_unsafe.disable_breaking_speed_check;

import com.unascribed.mirage.FabConf;
import net.minecraft.server.management.PlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.unascribed.mirage.support.EligibleIf;

@Mixin(PlayerInteractionManager.class)
@EligibleIf(configAvailable="*.disable_breaking_speed_check")
public abstract class MixinServerPlayerInteractionManager {

	@ModifyConstant(constant=@Constant(floatValue=0.7F), method="blockRemoving(Lnet/minecraft/util/math/BlockPos;)V")
	private float disableBreakingSpeedCheck(float old) {
		if (!FabConf.isEnabled("*.disable_breaking_speed_check")) return old;
		return 0.1F;
	}

}
