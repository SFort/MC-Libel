package com.unascribed.libel.mixin.e_mechanics.toggleable_furnace_carts;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityMinecartFurnace.class)
@EligibleIf(configAvailable="*.toggleable_furnace_carts")
public abstract class MixinFurnaceMinecartEntity extends EntityMinecart {

	@Shadow
	private int fuel;
	@Shadow
	public double pushX;
	@Shadow
	public double pushZ;

	public int fabrication$pauseFuel = 0;

	public MixinFurnaceMinecartEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At("HEAD"), method="moveAlongTrack(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)V")
	protected void toggleOnUnpoweredPoweredRail(BlockPos pos, IBlockState state, CallbackInfo ci) {
		if (!FabConf.isEnabled("*.toggleable_furnace_carts")) return;
		if (state.getBlock() == Blocks.GOLDEN_RAIL && !state.getValue(BlockRailPowered.POWERED)) {
			if (fuel > 0) {
				fabrication$pauseFuel += fuel;
				fuel = 0;
				pushX = 0;
				pushZ = 0;
			}
		} else if (fabrication$pauseFuel > 0) {
			fuel += fabrication$pauseFuel;
			fabrication$pauseFuel = 0;
			EnumFacing dir = this.getHorizontalFacing();
			pushX = dir.getFrontOffsetX();
			pushZ = dir.getFrontOffsetZ();
		}
	}

	@Inject(at=@At("TAIL"), method="writeEntityToNBT")
	protected void writeCustomDataToTag(NBTTagCompound nbt, CallbackInfo ci) {
		nbt.setInteger("fabrication:PauseFuel", fabrication$pauseFuel);
	}

	@Inject(at=@At("TAIL"), method="readEntityFromNBT")
	protected void readCustomDataFromTag(NBTTagCompound nbt, CallbackInfo ci) {
		fabrication$pauseFuel = nbt.getInteger("fabrication:PauseFuel");
	}

}
