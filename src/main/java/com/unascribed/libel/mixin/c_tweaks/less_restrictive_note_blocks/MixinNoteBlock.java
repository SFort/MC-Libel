package com.unascribed.libel.mixin.c_tweaks.less_restrictive_note_blocks;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.injection.ModifyReturn;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TileEntityNote.class)
@EligibleIf(configAvailable="*.less_restrictive_note_blocks")
public abstract class MixinNoteBlock extends TileEntity {

	@ModifyReturn(method="triggerNote(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V", target="Lnet/minecraft/block/state/IBlockState;getMaterial()Lnet/minecraft/block/material/Material;")
	private static Material fabrication$sidesAreValid(Material old, IBlockState state, Object self,  World world, BlockPos pos) {
		if (!FabConf.isEnabled("*.less_restrictive_note_blocks")) return old;
		EnumFacing[] directions = {
				EnumFacing.UP,
				EnumFacing.NORTH,
				EnumFacing.SOUTH,
				EnumFacing.WEST,
				EnumFacing.EAST,
				EnumFacing.DOWN
		};
		for (EnumFacing dir : directions) {
			BlockPos op = pos.offset(dir);
			IBlockState bs = world.getBlockState(op);
			if (!bs.isSideSolid(world, op, dir.getOpposite())) {
				return Material.AIR;
			}
		}
		return old;
	}

}
