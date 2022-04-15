package com.unascribed.libel.mixin.e_mechanics.anvil_repair;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockAnvil.class)
@EligibleIf(configAvailable="*.anvil_repair")
public class MixinAnvilBlock {

	@Inject(at=@At("HEAD"), method="onBlockActivated(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Z",
			cancellable=true)
	public void onUse(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> ci) {
		if (!FabConf.isEnabled("*.anvil_repair")) return;
		if (!world.isRemote) {
			ItemStack held = player.getHeldItem(hand);
			if (held.getItem() == Item.getItemFromBlock(Blocks.IRON_BLOCK)) {
				int dmg = state.getValue(BlockAnvil.DAMAGE);
				if (dmg > 0) {
					world.setBlockState(pos, state.withProperty(BlockAnvil.DAMAGE, dmg-1));
					world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1, 1);
					if (!player.capabilities.isCreativeMode) {
						held.shrink(1);
					}
					ci.setReturnValue(true);
				}
			}
		}
	}

}
