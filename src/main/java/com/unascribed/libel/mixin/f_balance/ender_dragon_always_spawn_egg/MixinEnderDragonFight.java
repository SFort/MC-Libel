package com.unascribed.libel.mixin.f_balance.ender_dragon_always_spawn_egg;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.world.end.DragonFightManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DragonFightManager.class)
@EligibleIf(configAvailable="*.ender_dragon_always_spawn_egg")
public class MixinEnderDragonFight {

	@Shadow
	private boolean previouslyKilled;

	@Inject(method="processDragonDeath(Lnet/minecraft/entity/boss/EntityDragon;)V", at=@At(value="INVOKE", target="Lnet/minecraft/world/end/DragonFightManager;spawnNewGateway()V"))
	public void dragonKilled(EntityDragon dragon, CallbackInfo ci){
		if (FabConf.isEnabled("*.ender_dragon_always_spawn_egg")){
			previouslyKilled = false;
		}
	}
}
