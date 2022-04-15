package com.unascribed.libel.mixin.d_minor_mechanics.feather_falling_five;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.ConfigPredicates;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
@EligibleIf(configAvailable="*.feather_falling_five")
public class MixinLivingEntity {

	@Inject(at=@At("HEAD"), method="damageEntity(Lnet/minecraft/util/DamageSource;F)V", cancellable=true)
	public void damage(DamageSource source, float amount, CallbackInfo ci) {
		if (!FabConf.isEnabled("*.feather_falling_five")) return;
		EntityLivingBase self = ((EntityLivingBase)(Object)this);
		ItemStack boots = self.getItemStackFromSlot(EntityEquipmentSlot.FEET);
		if (source == DamageSource.FALL && ConfigPredicates.shouldRun("*.feather_falling_five", self)) {
			if (FabConf.isEnabled("*.feather_falling_five_damages_boots") && ConfigPredicates.shouldRun("*.feather_falling_five_damages_boots", self) && !boots.isEmpty() && amount >= 2) {
				boots.damageItem((int)(amount/2), self);
			}
			ci.cancel();
		}
	}

}
