package com.unascribed.mirage.mixin.e_mechanics.enhanced_moistness;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.interfaces.MarkWet;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Entity.class)
@EligibleIf(configAvailable="*.enhanced_moistness")
public abstract class MixinEntity implements MarkWet {

	private int fabrication$wetTimer;
	private boolean fabrication$checkingOriginalWetness;

	@Shadow
	public World world;

	@Shadow
	public abstract boolean isWet();
	@Shadow
	public abstract boolean isInLava();

	@Shadow
	public double posX;

	@Shadow
	public double posY;

	@Shadow
	public double posZ;

	@Shadow
	@Nullable
	public abstract AxisAlignedBB getCollisionBoundingBox();

	@Inject(at=@At("TAIL"), method="onEntityUpdate()V")
	public void baseTick(CallbackInfo ci) {
		if (!FabConf.isEnabled("*.enhanced_moistness") || world == null || world.isRemote) return;
		if (isInLava()) {
			fabrication$wetTimer = 0;
		} else {
			try {
				fabrication$checkingOriginalWetness = true;
				if (isWet()) {
					fabrication$wetTimer = 100;
				} else if (fabrication$wetTimer > 0) {
					fabrication$wetTimer--;
					Object self = this;
					if (self instanceof EntityLivingBase) {
						if (fabrication$wetTimer%4 == 0) {
							AxisAlignedBB box = getCollisionBoundingBox();
							if (box == null) return;
							world.spawnParticle(EnumParticleTypes.DRIP_WATER, posX, posY+((box.maxY - box.minY)/2), posZ, (box.maxX - box.minX)/3, (box.maxY - box.minY)/4, (box.maxZ - box.minZ)/3);
						}
					}
				}
			} finally {
				fabrication$checkingOriginalWetness = false;
			}
		}
	}

	@Inject(at=@At("HEAD"), method="isWet()Z", cancellable=true)
	public void isWet(CallbackInfoReturnable<Boolean> ci) {
		if (FabConf.isEnabled("*.enhanced_moistness")) {
			if (fabrication$wetTimer > 0 && !fabrication$checkingOriginalWetness) {
				ci.setReturnValue(true);
			}
		}
	}

	@Override
	public void fabrication$markWet() {
		fabrication$wetTimer = 100;
	}

}
