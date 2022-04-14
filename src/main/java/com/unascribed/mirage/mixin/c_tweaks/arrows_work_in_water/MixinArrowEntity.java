package com.unascribed.mirage.mixin.c_tweaks.arrows_work_in_water;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.injection.ModifyReturn;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityArrow.class)
@EligibleIf(configAvailable="*.arrows_work_in_water")
public abstract class MixinArrowEntity extends Entity {

	public MixinArrowEntity(World worldIn) {
		super(worldIn);
	}

	@ModifyReturn(method="onUpdate()V", target="Lnet/minecraft/entity/projectile/EntityArrow;isInWater()Z")
	private static float fabrication$ignoreWater(float old) {
		return FabConf.isEnabled("*.arrows_work_in_water") ? 0.85f : old;
	}

}
