package com.unascribed.mirage.mixin.c_tweaks.campfires_cook_entities;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
@EligibleIf(configAvailable="*.campfires_cook_entities")
public class MixinLivingEntity {

	@Inject(at=@At("HEAD"), method="dropLoot(ZILnet/minecraft/util/DamageSource;)V")
	public void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source, CallbackInfo ci) {
		if (FabConf.isEnabled("*.campfires_cook_entities") && source == DamageSource.IN_FIRE) ((EntityLivingBase)(Object)this).setFire(1);
	}

}
