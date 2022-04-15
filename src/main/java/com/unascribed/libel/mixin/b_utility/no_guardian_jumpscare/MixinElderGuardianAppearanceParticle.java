package com.unascribed.libel.mixin.b_utility.no_guardian_jumpscare;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Env;
import net.minecraft.client.particle.ParticleMobAppearance;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleMobAppearance.class)
@EligibleIf(configAvailable="*.no_guardian_jumpscare", envMatches=Env.CLIENT)
public class MixinElderGuardianAppearanceParticle {

	@Inject(at=@At("HEAD"), method="renderParticle(Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/entity/Entity;FFFFFF)V", cancellable=true)
	public void buildGeometry(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, CallbackInfo ci) {
		if (FabConf.isEnabled("*.no_guardian_jumpscare"))
			ci.cancel();
	}

}
