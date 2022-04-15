package com.unascribed.libel.mixin.g_weird_tweaks.chaining_creepers;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
@EligibleIf(configAvailable="*.chaining_creepers")
public abstract class MixinLivingEntity {

	@Inject(method= "damageEntity", at=@At("HEAD"), cancellable=true)
	public void lightCreepersOnExplosion(DamageSource damageSrc, float damageAmount, CallbackInfo cir) {
		Object self = this;
		if (!(FabConf.isEnabled("*.chaining_creepers") && self instanceof EntityCreeper && damageSrc.isExplosion())) return;
		((EntityCreeper)self).ignite();
		cir.cancel();
	}
}
