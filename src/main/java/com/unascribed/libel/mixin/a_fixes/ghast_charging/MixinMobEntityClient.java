package com.unascribed.libel.mixin.a_fixes.ghast_charging;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.interfaces.GhastAttackTime;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Env;
import net.minecraft.entity.monster.EntityGhast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityGhast.class)
@EligibleIf(configAvailable="*.ghast_charging", envMatches=Env.CLIENT)
public class MixinMobEntityClient implements GhastAttackTime {

	@Unique
	private int fabrication$ghastAttackTime;

	@Inject(at=@At("TAIL"), method="onUpdate()V")
	public void tick(CallbackInfo ci) {
		if (!FabConf.isEnabled("*.ghast_charging")) return;
		EntityGhast g = (EntityGhast)(Object)this;
		if (g.isAttacking()) {
			if (g.isEntityAlive()) {
				fabrication$ghastAttackTime++;
			}
		} else {
			fabrication$ghastAttackTime = 0;
		}
	}

	@Override
	public int getAttackTime() {
		return fabrication$ghastAttackTime;
	}

}
