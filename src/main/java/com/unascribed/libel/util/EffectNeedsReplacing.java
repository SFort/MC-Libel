package com.unascribed.libel.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;

public class EffectNeedsReplacing {
	public static boolean needsReplacing(EntityPlayer pe, Potion se) {
		return !pe.isPotionActive(se) || !pe.getActivePotionEffect(se).getIsAmbient() || pe.getActivePotionEffect(se).doesShowParticles();
	}
}
