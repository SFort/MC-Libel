package com.unascribed.mirage.mixin.i_woina.no_sprint;

import com.unascribed.mirage.FabConf;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.mirage.support.EligibleIf;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

@Mixin(EntityLivingBase.class)
@EligibleIf(configAvailable="*.no_sprint")
abstract public class MixinLivingEntity extends Entity {

	public MixinLivingEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At("HEAD"), method="setSprinting(Z)V", cancellable = true)
	public void setSprinting(boolean sprinting, CallbackInfo ci) {
		if (FabConf.isEnabled("*.no_sprint")) {
			super.setSprinting(false);
			ci.cancel();
		}
	}
}
