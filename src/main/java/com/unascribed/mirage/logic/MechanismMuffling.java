package com.unascribed.mirage.logic;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MechanismMuffling {

	public static boolean isMuffled(World subject, BlockPos pos) {
		for (EnumFacing dir : EnumFacing.values()) {
			if (subject.getBlockState(pos.offset(dir)).getBlock() == Blocks.WOOL) {
				return true;
			}
		}
		return false;
	}

}
