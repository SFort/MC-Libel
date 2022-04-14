package com.unascribed.mirage.mixin.g_weird_tweaks.instant_pickup;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.logic.InstantPickup;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
@EligibleIf(configAvailable="*.instant_pickup")
public class MixinBlock {

	@Inject(at=@At("TAIL"), method="harvestBlock(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/tileentity/TileEntity;Lnet/minecraft/item/ItemStack;)V")
	private static void dropStacks(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack, CallbackInfo ci) {
		if (FabConf.isEnabled("*.instant_pickup") && player instanceof EntityPlayer) {
			InstantPickup.slurp(world, new AxisAlignedBB(pos).expand(0.25, 0.25, 0.25), (EntityPlayer)player);
		}
	}


}
