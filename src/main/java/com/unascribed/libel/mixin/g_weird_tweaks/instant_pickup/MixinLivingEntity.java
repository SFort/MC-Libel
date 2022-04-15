package com.unascribed.libel.mixin.g_weird_tweaks.instant_pickup;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.logic.InstantPickup;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
@EligibleIf(configAvailable="*.instant_pickup")
public abstract class MixinLivingEntity extends Entity {

	public MixinLivingEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At("TAIL"), method="onDeath(Lnet/minecraft/util/DamageSource;)V")
	public void onDeath(DamageSource src, CallbackInfo ci) {
		if (FabConf.isEnabled("*.instant_pickup") && src.getTrueSource() instanceof EntityPlayer) {
			InstantPickup.slurp(world, getCollisionBoundingBox().expand(0.25, 0.25, 0.25), (EntityPlayer)src.getTrueSource());
		}
	}

}
