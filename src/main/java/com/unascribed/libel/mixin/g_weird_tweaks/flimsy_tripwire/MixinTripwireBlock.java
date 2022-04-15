package com.unascribed.libel.mixin.g_weird_tweaks.flimsy_tripwire;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.injection.Hijack;
import net.minecraft.block.BlockTripWire;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockTripWire.class)
@EligibleIf(configAvailable="*.flimsy_tripwire")
public abstract class MixinTripwireBlock {

	@Hijack(target="Lnet/minecraft/block/BlockTripWire;updateState(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V",
			method="updateTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Ljava/util/Random;)V")
	private static boolean fabrication$breakWire(BlockTripWire self, World world, BlockPos pos){
		if (!(FabConf.isEnabled("*.flimsy_tripwire") && world.getBlockState(pos).getValue(BlockTripWire.ATTACHED))) return false;
		world.destroyBlock(pos, true);
		return true;
	}

}
