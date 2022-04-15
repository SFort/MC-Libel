package com.unascribed.mirage.mixin.c_tweaks.no_hunger;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.ConfigPredicates;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.injection.Hijack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemFood.class)
@EligibleIf(configAvailable="*.no_hunger")
public abstract class MixinPlayerEntity {

	@Hijack(method="onItemUseFinish(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/item/ItemStack;", target="Lnet/minecraft/util/FoodStats;addStats(Lnet/minecraft/item/ItemFood;Lnet/minecraft/item/ItemStack;)V")
	private static boolean eatFood(FoodStats stats, ItemFood food, ItemStack stack, Object self, ItemStack stack2, World world, EntityLivingBase entity) {
		if (FabConf.isEnabled("*.no_hunger") && entity instanceof EntityPlayer) {
			if (ConfigPredicates.shouldRun("*.no_hunger", (EntityPlayer)entity)) {
				entity.heal((food.getHealAmount(stack)+food.getSaturationModifier(stack))*0.75f);
				return true;
			}
		}
		return false;
	}

}
