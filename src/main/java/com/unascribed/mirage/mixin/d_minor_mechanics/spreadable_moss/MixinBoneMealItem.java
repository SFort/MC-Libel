package com.unascribed.mirage.mixin.d_minor_mechanics.spreadable_moss;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemDye.class)
@EligibleIf(configAvailable="*.spreadable_moss")
public class MixinBoneMealItem {

	@Inject(at=@At("HEAD"), method="applyBonemeal(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Z", cancellable=true)
	private static void useOnBlock(ItemStack stack, World world, BlockPos bp, EntityPlayer target, EnumHand player, CallbackInfoReturnable<Boolean> cir) {
		if (!FabConf.isEnabled("*.spreadable_moss")) return;

		if (world.getBlockState(bp).getBlock() == Blocks.COBBLESTONE) {
			boolean found = false;
			for (BlockPos cur : BlockPos.getAllInBox(bp.add(-1,-1,-1), bp.add(1,1,1))) {
				if (world.getBlockState(cur).getBlock() == Blocks.MOSSY_COBBLESTONE) {
					found = true;
					break;
				}
			}
			if (found) {
				if (!world.isRemote) {
					stack.shrink(1);
					world.setBlockState(bp, Blocks.MOSSY_COBBLESTONE.getDefaultState());
				}
				cir.setReturnValue(true);
			}
		}
	}

}
