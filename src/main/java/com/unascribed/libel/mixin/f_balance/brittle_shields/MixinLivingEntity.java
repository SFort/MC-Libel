package com.unascribed.libel.mixin.f_balance.brittle_shields;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
@EligibleIf(configAvailable="*.brittle_shields")
public abstract class MixinLivingEntity {

	@Shadow
	protected ItemStack activeItemStack;

	@Shadow
	protected abstract void damageShield(float amount);

	@Inject(method="attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z",
			at=@At(value="INVOKE", target="Lnet/minecraft/entity/EntityLivingBase;damageShield(F)V"))
	public void brittleShield(DamageSource source, float b0, CallbackInfoReturnable<Boolean> cir) {
		if (!(FabConf.isEnabled("*.brittle_shields") && source.isExplosion())) return;
		damageShield(activeItemStack.getItem().getMaxDamage());
	}
}
