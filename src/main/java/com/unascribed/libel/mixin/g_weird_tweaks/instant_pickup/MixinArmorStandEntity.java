package com.unascribed.libel.mixin.g_weird_tweaks.instant_pickup;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.logic.InstantPickup;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityArmorStand.class)
@EligibleIf(configAvailable="*.instant_pickup")
public abstract class MixinArmorStandEntity extends EntityLivingBase {

	public MixinArmorStandEntity(World worldIn) {
		super(worldIn);
	}

	// grr...

	@Inject(at=@At("TAIL"), method="attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z")
	private void onBreak(DamageSource src, float flag1, CallbackInfoReturnable<Boolean> cir) {
		if (FabConf.isEnabled("*.instant_pickup") && src.getTrueSource() instanceof EntityPlayer) {
			InstantPickup.slurp(world, this.getEntityBoundingBox().expand(0.25, 0.25, 0.25), (EntityPlayer)src.getTrueSource());
		}
	}

}
