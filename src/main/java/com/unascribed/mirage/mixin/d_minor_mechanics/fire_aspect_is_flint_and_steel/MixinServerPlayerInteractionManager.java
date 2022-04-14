package com.unascribed.mirage.mixin.d_minor_mechanics.fire_aspect_is_flint_and_steel;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInteractionManager.class)
@EligibleIf(configAvailable="*.fire_aspect_is_flint_and_steel")
public class MixinServerPlayerInteractionManager {

	@Inject(at=@At("RETURN"), method="processRightClickBlock(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;FFF)Lnet/minecraft/util/EnumActionResult;",
			cancellable=true)
	public void interactBlock(EntityPlayer player, World world, ItemStack stack, EnumHand hand, BlockPos pos, EnumFacing j, float i, float copyBeforeUse, float reachDist, CallbackInfoReturnable<EnumActionResult> ci) {
		if (FabConf.isEnabled("*.fire_aspect_is_flint_and_steel") && ci.getReturnValue() == EnumActionResult.PASS) {
			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack) > 0) {
				PlayerInteractionManager self = (PlayerInteractionManager)(Object)this;
				ItemStack flintAndSteel = new ItemStack(Items.FLINT_AND_STEEL);
				try {
					player.setHeldItem(hand, flintAndSteel);
					EnumActionResult ar = self.processRightClickBlock(player, world, flintAndSteel, hand, pos, j, i, copyBeforeUse, reachDist);
					if (ar == EnumActionResult.SUCCESS) {
						player.swingArm(hand);
						world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1, world.rand.nextFloat() * 0.4f + 0.8f);
					}
					if (flintAndSteel.isItemDamaged()) {
						stack.damageItem(flintAndSteel.getItemDamage(), player);
					}
					ci.setReturnValue(ar);
				} finally {
					player.setHeldItem(hand, stack);
				}
			}
		}
	}


}
