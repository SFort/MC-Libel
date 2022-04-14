package com.unascribed.mirage.mixin.c_tweaks.normal_fog_with_night_vision;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.Env;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
@EligibleIf(configAvailable="*.normal_fog_with_night_vision", envMatches=Env.CLIENT)
public class MixinBackgroundRenderer {

	@Inject(at=@At("HEAD"), method="getNightVisionBrightness(Lnet/minecraft/entity/EntityLivingBase;F)F")
	public void getNightVisionStrength(EntityLivingBase entitylivingbaseIn, float partialTicks, CallbackInfoReturnable<Float> cir) {
		if (FabConf.isEnabled("*.normal_fog_with_night_vision")) {
			int i = entitylivingbaseIn.getActivePotionEffect(MobEffects.NIGHT_VISION).getDuration();
			cir.setReturnValue(1.0F + MathHelper.sin(((float)i - partialTicks) * (float)Math.PI * 0.2F) * 0.3F);
		}
	}

}
