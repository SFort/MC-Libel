package com.unascribed.libel.mixin.f_balance.environmentally_friendly_creepers;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.monster.EntityCreeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityCreeper.class)
@EligibleIf(configAvailable="*.environmentally_friendly_creepers")
public class MixinCreeperEntity {

	@ModifyArg(at=@At(value="INVOKE", target="Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFZ)Lnet/minecraft/world/Explosion;"),
			method="explode()V")
	public boolean nonMobGriefingDestructionType(boolean o) {
		return !FabConf.isEnabled("*.environmentally_friendly_creepers") && o;
	}

}
