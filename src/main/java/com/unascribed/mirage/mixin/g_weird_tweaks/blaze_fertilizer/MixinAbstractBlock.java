package com.unascribed.mirage.mixin.g_weird_tweaks.blaze_fertilizer;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
@EligibleIf(configAvailable="*.blaze_fertilizer")
public abstract class MixinAbstractBlock {

	@Inject(method="onBlockActivated(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Z",
			at=@At("HEAD"), cancellable=true)
	public void onUse(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir) {
		ItemStack items = player.getHeldItem(hand);
		if (FabConf.isEnabled("*.blaze_fertilizer") && world instanceof WorldServer && items.getItem().equals(Items.BLAZE_POWDER)
				&& state.getBlock().equals(Blocks.NETHER_WART) && state.getValue(BlockNetherWart.AGE) < 3) {
			world.setBlockState(pos, state.withProperty(BlockNetherWart.AGE, Math.min(world.rand.nextInt(3) + state.getValue(BlockNetherWart.AGE), 3)), 2);
			((WorldServer)world).spawnParticle(EnumParticleTypes.FLAME, pos.getX()+0.5, pos.getY()+0.4, pos.getZ()+0.5, 4, 0.3, 0.3, 0.3, 0.05);
			if (!player.isCreative()) {
				items.shrink(1);
			}
			cir.setReturnValue(true);
		}
	}
}
