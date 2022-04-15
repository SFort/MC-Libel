package com.unascribed.libel.mixin.f_balance.pickup_skeleton_arrows;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.interfaces.TagStrayArrow;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.projectile.EntityArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityStray.class)
@EligibleIf(configAvailable="*.pickup_skeleton_arrows")
public abstract class MixinStrayEntity {

	@ModifyVariable(at=@At("STORE"), method="getArrow(F)Lnet/minecraft/entity/projectile/EntityArrow;")
	public EntityArrow createArrowProjectile(EntityArrow arrow) {
		if(FabConf.isEnabled("*.pickup_skeleton_arrows") && arrow instanceof TagStrayArrow) {
			((TagStrayArrow)arrow).fabrication$firedByStray();
		}
		return arrow;
	}
}
