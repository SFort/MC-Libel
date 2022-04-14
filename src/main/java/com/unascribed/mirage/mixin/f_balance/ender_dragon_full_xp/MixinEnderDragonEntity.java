package com.unascribed.mirage.mixin.f_balance.ender_dragon_full_xp;

import com.unascribed.mirage.FabConf;
import net.minecraft.entity.boss.EntityDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.unascribed.mirage.support.EligibleIf;

@Mixin(EntityDragon.class)
@EligibleIf(configAvailable="*.ender_dragon_full_xp")
public class MixinEnderDragonEntity {

	@ModifyVariable(method="onDeathUpdate", at=@At(value="STORE"))
	private int fullXp(int old){
		return FabConf.isEnabled("*.ender_dragon_full_xp") && old == 500 ? 1500 : old;
	}
}
