package com.unascribed.libel.mixin.e_mechanics.detecting_powered_rails;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailDetector;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(BlockRailPowered.class)
@EligibleIf(configAvailable="*.detecting_powered_rails")
public abstract class MixinPoweredRailBlock extends BlockRailBase {

	protected MixinPoweredRailBlock(boolean isPowered) {
		super(isPowered);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
		if (!FabConf.isEnabled("*.detecting_powered_rails")) return 0;
		return Blocks.DETECTOR_RAIL.getComparatorInputOverride(Blocks.DETECTOR_RAIL.getDefaultState().withProperty(BlockRailDetector.POWERED, true), world, pos);
	}

	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing direction) {
		if (!FabConf.isEnabled("*.detecting_powered_rails")) return 0;
		if (!(world instanceof World)) return 0;
		IBlockState who = world.getBlockState(pos.offset(direction.getOpposite()));
		if (!(who.getBlock() == Blocks.POWERED_REPEATER || who.getBlock() == Blocks.UNPOWERED_REPEATER)) return 0;
		if (who.getValue(BlockRedstoneRepeater.FACING) != direction) return 0;
		return ((AccessorDetectorRailBlock)Blocks.DETECTOR_RAIL).fabrication$findMinecarts((World)world, pos, EntityMinecart.class, e -> true).isEmpty() ? 0 : 15;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if (!FabConf.isEnabled("*.detecting_powered_rails")) return;
		world.updateComparatorOutputLevel(pos, this);
		for (EnumFacing d : EnumFacing.HORIZONTALS) {
			BlockPos ofs = pos.offset(d);
			IBlockState bs = world.getBlockState(ofs);
			if ((bs.getBlock() == Blocks.POWERED_REPEATER || bs.getBlock() == Blocks.UNPOWERED_REPEATER) && bs.getValue(BlockRedstoneRepeater.FACING) == d.getOpposite()) {
				bs.neighborChanged(world, ofs, this, pos);
			}
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		if (!FabConf.isEnabled("*.detecting_powered_rails")) return;
		if (!world.isRemote) {
			if (entity instanceof EntityMinecart) {
				for (EnumFacing d : EnumFacing.HORIZONTALS) {
					BlockPos ofs = pos.offset(d);
					if (!world.isBlockLoaded(ofs)) continue;
					IBlockState bs = world.getBlockState(ofs);
					if ((bs.getBlock() == Blocks.POWERED_REPEATER || bs.getBlock() == Blocks.UNPOWERED_REPEATER) && bs.getValue(BlockRedstoneRepeater.FACING) == d.getOpposite()) {
						bs.neighborChanged(world, ofs, this, pos);
					}
				}
			}
			world.scheduleBlockUpdate(pos, this, 20, 0);
			world.updateComparatorOutputLevel(pos, this);
		}
	}


}
