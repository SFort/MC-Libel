package com.unascribed.libel.mixin.a_fixes.melee_mobs_keep_attacking;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.injection.ModifyReturn;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.pathfinding.Path;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityAIAttackMelee.class)
@EligibleIf(configAvailable="*.melee_mobs_keep_attacking")
public abstract class MixinMeleeAttackGoal {

	@Shadow
	protected EntityCreature attacker;

	@Shadow
	Path path;

	@Shadow
	public abstract void startExecuting();

	@ModifyReturn(target="Lnet/minecraft/pathfinding/PathNavigate;noPath()Z", method="shouldContinueExecuting()Z")
	public boolean fabrication$keepAttacking(boolean old) {
		if (!FabConf.isEnabled("*.melee_mobs_keep_attacking")) return old;
		if (old && this.attacker.getAttackTarget() != null && this.attacker.getDistance(this.attacker.getAttackTarget()) < 10) {
			this.path = this.attacker.getNavigator().getPathToEntityLiving(this.attacker.getAttackTarget());
			startExecuting();
		}
		return false;
	}

}
