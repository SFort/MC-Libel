package com.unascribed.libel.mixin.d_minor_mechanics.note_blocks_play_on_landing;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNote;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockNote.class)
@EligibleIf(configAvailable="*.note_blocks_play_on_landing")
public abstract class MixinNoteBlock extends Block {

	public MixinNoteBlock(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
	}

	@Override
	public void onFallenUpon(World world, BlockPos pos, Entity entity, float distance) {
		super.onFallenUpon(world, pos, entity, distance);
		if (!world.isRemote && FabConf.isEnabled("*.note_blocks_play_on_landing")) {
			TileEntity te = world.getTileEntity(pos);
			if (!(te instanceof TileEntityNote)) return;
			for (int i = 0; i < Math.min(8, Math.ceil(distance/2)); i++) {
				((TileEntityNote)te).triggerNote(world, pos);
			}
		}
	}

}
