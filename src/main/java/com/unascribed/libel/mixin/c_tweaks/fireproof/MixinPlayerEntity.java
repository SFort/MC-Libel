package com.unascribed.libel.mixin.c_tweaks.fireproof;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.ConfigPredicates;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
@EligibleIf(configAvailable="*.fireproof")
public abstract class MixinPlayerEntity {

	@Inject(at=@At("HEAD"), method="isEntityInvulnerable(Lnet/minecraft/util/DamageSource;)Z", cancellable=true)
	public void isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if (((Object)this) instanceof EntityPlayer && FabConf.isEnabled("*.fireproof")) {
			if (ConfigPredicates.shouldRun("*.fireproof", (EntityPlayer)(Object)this) && source.isFireDamage()) {
				cir.setReturnValue(true);
			}
		}
	}

}
