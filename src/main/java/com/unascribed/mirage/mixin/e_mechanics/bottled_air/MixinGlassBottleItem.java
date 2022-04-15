package com.unascribed.mirage.mixin.e_mechanics.bottled_air;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemGlassBottle.class)
@EligibleIf(configAvailable="*.bottled_air")
public class MixinGlassBottleItem {

	@Inject(at=@At(value="FIELD", target="Lnet/minecraft/init/Items;POTIONITEM:Lnet/minecraft/item/ItemPotion;"), method="onItemRightClick(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/ActionResult;")
	public void fabrication$readState(World worldIn, EntityPlayer player, EnumHand handIn, CallbackInfoReturnable<ActionResult<ItemStack>> cir) {
		if (FabConf.isEnabled("*.bottled_air") && player.isInWater()) {
			if (player.getAir() < 300) {
				player.setAir(Math.min(300, player.getAir()+30));
			}
		}
	}

}
