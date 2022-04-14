package com.unascribed.mirage.mixin.c_tweaks.can_breathe_water;

import com.mojang.authlib.GameProfile;
import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.ConfigPredicates;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerMP.class)
@EligibleIf(configAvailable="*.can_breathe_water")
public abstract class MixinServerPlayerEntity extends EntityPlayer {

	public MixinServerPlayerEntity(World worldIn, GameProfile gameProfileIn) {
		super(worldIn, gameProfileIn);
	}

	@Inject(at=@At("TAIL"), method="onUpdate()V")
	public void tick(CallbackInfo ci) {
		if (FabConf.isEnabled("*.can_breathe_water") && ConfigPredicates.shouldRun("*.can_breathe_water", (EntityPlayer)this)) {
			setAir(300);
		}
	}

}
