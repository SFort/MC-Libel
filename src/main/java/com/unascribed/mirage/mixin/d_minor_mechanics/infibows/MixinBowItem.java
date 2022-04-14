package com.unascribed.mirage.mixin.d_minor_mechanics.infibows;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemBow.class)
@EligibleIf(configAvailable="*.infibows", classNotPresent="net.parker8283.bif.BowInfinityFix")
public class MixinBowItem {

	@ModifyVariable(at=@At("STORE"), method="onItemRightClick(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/ActionResult;")
	private boolean infiBow(boolean hasArrows, World world, EntityPlayer player, EnumHand hand) {
		if (FabConf.isEnabled("*.infibows") && EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, player.getHeldItem(hand)) > 0)
			return true;
		return hasArrows;
	}

}
