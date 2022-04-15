package com.unascribed.libel.mixin.g_weird_tweaks.item_safe_cactus;

import com.unascribed.libel.FabConf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.libel.support.EligibleIf;

@Mixin(EntityItem.class)
@EligibleIf(configAvailable="*.item_safe_cactus")
public class MixinItemEntity {

	@Inject(method = "attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
	public void onEntityCollision(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (FabConf.isEnabled("*.item_safe_cactus") && source.equals(DamageSource.CACTUS))
			cir.setReturnValue(false);
	}
}
