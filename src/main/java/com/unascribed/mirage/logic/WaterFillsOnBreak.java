package com.unascribed.mirage.logic;

import com.google.common.collect.ImmutableSet;
import com.unascribed.mirage.FabConf;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WaterFillsOnBreak {

	private static final ImmutableSet<EnumFacing> CHECK_DIRECTIONS = ImmutableSet.of(
			EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST
			);

	public static boolean shouldFill(World world, BlockPos pos) {
		int countWater = 0;
		int countAir = 0;
		BlockPos lastWater = null;
		for (EnumFacing d : CHECK_DIRECTIONS) {
			BlockPos p = pos.offset(d);
			Block bs = world.getBlockState(p).getBlock();
			if (bs == Blocks.WATER) {
				lastWater = p;
				countWater++;
			} else if (d != EnumFacing.UP) {
				if (bs == Blocks.AIR) {
					countAir++;
				}
			}
		}
		if (!(FabConf.isEnabled("*.water_fills_on_break_strict") && countWater == 1)){
			return countWater > countAir;
		}

		for (EnumFacing d : EnumFacing.values()) {
			if (world.getBlockState(lastWater.offset(d)).getBlock() == Blocks.WATER) {
				return true;
			}
		}
		return false;
	}

}
