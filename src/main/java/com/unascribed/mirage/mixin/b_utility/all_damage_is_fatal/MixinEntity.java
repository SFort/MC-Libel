package com.unascribed.mirage.mixin.b_utility.all_damage_is_fatal;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.ConfigPredicates;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityLivingBase.class)
@EligibleIf(configAvailable="*.all_damage_is_fatal")
public abstract class MixinEntity {

	@ModifyVariable(at=@At("HEAD"), method="damageEntity(Lnet/minecraft/util/DamageSource;F)V", index=2, argsOnly=true)
	public float adjustDamage(float amount) {
		if (amount == 0) return 0;
		EntityLivingBase le = ((EntityLivingBase)(Object)this);
		return FabConf.isEnabled("*.all_damage_is_fatal") && ConfigPredicates.shouldRun("*.all_damage_is_fatal", le) ? le.getHealth()*20 : amount;
	}

}
