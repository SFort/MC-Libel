package com.unascribed.libel.mixin.c_tweaks.nether_cauldron;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockCauldron.class)
@EligibleIf(configAvailable="*.nether_cauldron")
public class MixinAbstractCauldronBlock {
	@Inject(at=@At("TAIL"), method="setWaterLevel(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)V", cancellable=true)
	private void setLevel(World world, BlockPos pos, IBlockState state, int level, CallbackInfo ci) {
		if (FabConf.isEnabled("*.nether_cauldron") && world.provider.getDimension() == -1) {
			if (level == 0) return;
			if (state.getValue(BlockCauldron.LEVEL) > 0) world.setBlockState(pos, state.withProperty(BlockCauldron.LEVEL, 0), 2);
			world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 2.6f + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8f);
			if (world instanceof WorldServer)
				((WorldServer)world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX()+0.5, pos.getY()+0.6, pos.getZ()+0.5, 8, 0.2, 0.2, 0.2, 0);
			ci.cancel();
		}
	}
}
