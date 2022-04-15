package com.unascribed.libel.mixin.a_fixes.stable_cacti;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Iterator;

@Mixin(BlockCactus.class)
@EligibleIf(configAvailable="*.stable_cacti")
public class MixinCactusBlock extends Block {

	public MixinCactusBlock(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
	}

	@ModifyVariable(at=@At("STORE"), method="canBlockStay(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z")
	public Iterator<EnumFacing> returnEmptyIter(Iterator<EnumFacing> old) {
		if (FabConf.isEnabled("*.stable_cacti")) return Collections.emptyIterator();
		return old;
	}

	@Inject(at=@At("HEAD"), method="neighborChanged(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;)V")
	public void getStateForNeighborUpdate(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, CallbackInfo ci) {
		if (!FabConf.isEnabled("*.stable_cacti")) return;
		if (fromPos.getY() > pos.getY() && state.getBlock() == this && state.getValue(BlockCactus.AGE) > 0) {
			IBlockState newState = world.getBlockState(fromPos);
			if (newState.getBlock() == this && newState.getValue(BlockCactus.AGE) == 0) {
				// we just grew
				boolean shouldBreak = false;
				for (EnumFacing d : EnumFacing.Plane.HORIZONTAL) {
					BlockPos p = fromPos.offset(d);
					IBlockState bs = world.getBlockState(p);
					Material m = bs.getMaterial();
					if (bs.getBlock() != this && (m.isSolid() || m == Material.LAVA)) {
						world.destroyBlock(fromPos, true);
						break;
					}
				}
			}
		}
	}

}
