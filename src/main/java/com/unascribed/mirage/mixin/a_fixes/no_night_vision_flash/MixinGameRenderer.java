package com.unascribed.mirage.mixin.a_fixes.no_night_vision_flash;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.Env;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
@EligibleIf(configAvailable="*.no_night_vision_flash", envMatches=Env.CLIENT)
public class MixinGameRenderer {
	@Inject(method="getNightVisionBrightness(Lnet/minecraft/entity/EntityLivingBase;F)F", at=@At("RETURN"), cancellable=true)
	public void fabrication$removeFlash(EntityLivingBase entitylivingbaseIn, float partialTicks, CallbackInfoReturnable<Float> cir) {
		if (FabConf.isEnabled("*.no_night_vision_flash")) {
			float time = (cir.getReturnValueF()/((float)Math.PI*0.2f));
			if (time < 0) time = 0;
			float a = (time/200f);
			a = a*a; // exponential falloff
			cir.setReturnValue((a-0.7f)/0.3f);
		}
	}

}
