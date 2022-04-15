package com.unascribed.libel.mixin.f_balance.pickup_skeleton_arrows;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.projectile.EntityArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSkeleton.class)
@EligibleIf(configAvailable="*.pickup_skeleton_arrows")
public abstract class MixinAbstractSkeletonEntity {
	@Inject(at=@At("RETURN"), method= "getArrow(F)Lnet/minecraft/entity/projectile/EntityArrow;", cancellable=true)
	public void createArrowProjectile(float p_190726_1_, CallbackInfoReturnable<EntityArrow> cir) {
		if(!FabConf.isEnabled("*.pickup_skeleton_arrows")) return;

		EntityArrow arrowEntity = cir.getReturnValue();
		arrowEntity.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
		cir.setReturnValue(arrowEntity);
	}
}
