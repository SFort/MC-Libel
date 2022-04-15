package com.unascribed.libel.mixin.c_tweaks.no_hunger;

import com.mojang.authlib.GameProfile;
import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.ConfigPredicates;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerMP.class)
@EligibleIf(configAvailable="*.no_hunger")
public abstract class MixinServerPlayerEntity extends EntityPlayer {

	public MixinServerPlayerEntity(World worldIn, GameProfile gameProfileIn) {
		super(worldIn, gameProfileIn);
	}

	@Inject(at=@At("TAIL"), method="onUpdate()V")
	public void tick(CallbackInfo ci) {
		if (FabConf.isEnabled("*.no_hunger") && ConfigPredicates.shouldRun("*.no_hunger", (EntityPlayer)this)) {
			getFoodStats().setFoodLevel(this.isPotionActive(MobEffects.HUNGER) ? 0 : getHealth() >= getMaxHealth() ? 20 : 17);
			getFoodStats().setFoodSaturationLevel(10);
		}
	}

}
