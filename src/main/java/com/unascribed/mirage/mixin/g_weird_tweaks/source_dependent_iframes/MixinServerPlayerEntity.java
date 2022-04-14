package com.unascribed.mirage.mixin.g_weird_tweaks.source_dependent_iframes;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.interfaces.TickSourceIFrames;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerMP.class)
@EligibleIf(configAvailable="*.source_dependent_iframes")
public abstract class MixinServerPlayerEntity {

	@Inject(at=@At("HEAD"), method= "onUpdateEntity()V")
	private void tickSourceDependentIFrames(CallbackInfo ci) {
		if (!FabConf.isEnabled("*.source_dependent_iframes")) return;
		((TickSourceIFrames)this).fabrication$tickSourceDependentIFrames();
	}

}
