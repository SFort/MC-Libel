package com.unascribed.mirage.mixin.c_tweaks.shulker_bullets_despawn_on_death;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityShulkerBullet.class)
@EligibleIf(configAvailable="*.shulker_bullets_despawn_on_death")
public abstract class MixinShulkerBulletEntity {

	@Shadow
	private EntityLivingBase owner;

	@Inject(at=@At("HEAD"), method="onUpdate()V", cancellable=true)
	public void tick(CallbackInfo ci) {
		Object self = this;
		if (FabConf.isEnabled("*.shulker_bullets_despawn_on_death") && !((Entity)self).world.isRemote) {
			if (owner == null || owner.isDead) {
				((Entity)self).setDead();
				ci.cancel();
			}
		}
	}

}
