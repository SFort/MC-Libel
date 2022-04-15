package com.unascribed.libel.mixin.i_woina.no_player_death_animation;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Env;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLivingBase.class)
@EligibleIf(configAvailable="*.no_player_death_animation", envMatches=Env.CLIENT)
public abstract class MixinLivingEntityRenderer<T extends EntityLivingBase> {

	@Inject(method="getDeathMaxRotation(Lnet/minecraft/entity/EntityLivingBase;)F", at=@At("HEAD"), cancellable = true)
	public void oldDeath(T instance, CallbackInfoReturnable<Float> cir){
		if (FabConf.isEnabled("*.no_player_death_animation") && instance instanceof EntityPlayer) cir.setReturnValue(0f);
	}

}
