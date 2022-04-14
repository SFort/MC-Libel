package com.unascribed.mirage.mixin.f_balance.disable_mending;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.ConfigPredicates;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.injection.ModifyReturn;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityXPOrb.class)
@EligibleIf(configAvailable="*.disable_mending")
public class MixinExperienceOrbEntity {

	@ModifyReturn(method="onCollideWithPlayer(Lnet/minecraft/entity/player/EntityPlayer;)V", target="Lnet/minecraft/item/ItemStack;isItemDamaged()Z")
	private static boolean fabbrication$no_repair(boolean old, ItemStack stack, Object self, EntityPlayer player) {
		return !FabConf.isEnabled("*.disable_mending") || !ConfigPredicates.shouldRun("*.disable_mending", player);
	}

}
