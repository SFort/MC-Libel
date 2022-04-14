package com.unascribed.mirage.mixin.e_mechanics.enhanced_moistness;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.interfaces.MarkWet;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EntityPotion.class)
@EligibleIf(configAvailable="*.enhanced_moistness")
public abstract class MixinPotionEntity extends EntityThrowable {

	public MixinPotionEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At("TAIL"), method="applyWater()V", locals=LocalCapture.CAPTURE_FAILHARD)
	public void damageEntitiesHurtByWater(CallbackInfo ci, AxisAlignedBB box) {
		if (!FabConf.isEnabled("*.enhanced_moistness") || world.isRemote) return;
		for (Entity e : world.getEntitiesWithinAABB(Entity.class, box)) {
			if (e instanceof MarkWet) {
				((MarkWet)e).fabrication$markWet();
			}
			if (e instanceof EntityEnderman) {
				((EntityEnderman) e).setAttackTarget(null);
			}
		}
	}

}
