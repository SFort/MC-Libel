package com.unascribed.libel.mixin.d_minor_mechanics.fire_aspect_is_flint_and_steel;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
@EligibleIf(configAvailable="*.fire_aspect_is_flint_and_steel")
public class MixinPlayerEntity {

	@Inject(at=@At("RETURN"), method="interactOn(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/EnumActionResult;",
			cancellable=true)
	public void interact(Entity e, EnumHand hand, CallbackInfoReturnable<EnumActionResult> ci) {
		if (FabConf.isEnabled("*.fire_aspect_is_flint_and_steel") && ci.getReturnValue() == EnumActionResult.PASS) {
			EntityPlayer self = (EntityPlayer)(Object)this;
			ItemStack stack = self.getHeldItem(hand);
			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack) > 0) {
				ItemStack flintAndSteel = new ItemStack(Items.FLINT_AND_STEEL);
				try {
					self.setHeldItem(hand, flintAndSteel);
					EnumActionResult ar = self.interactOn(e, hand);
					if (ar == EnumActionResult.SUCCESS) {
						self.swingArm(hand);
						self.world.playSound(null, e.posX, e.posY, e.posZ, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1, self.world.rand.nextFloat() * 0.4f + 0.8f);
					}
					if (flintAndSteel.isItemDamaged()) {
						stack.damageItem(flintAndSteel.getItemDamage(), self);
					}
					ci.setReturnValue(ar);
				} finally {
					self.setHeldItem(hand, stack);
				}
			}
		}
	}


}
