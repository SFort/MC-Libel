package com.unascribed.libel.mixin.d_minor_mechanics.water_fills_on_break;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.logic.WaterFillsOnBreak;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.init.Blocks;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInteractionManager.class)
@EligibleIf(anyConfigAvailable={"*.water_fills_on_break", "*.water_fills_on_break_strict"})
public class MixinServerPlayerInteractionManager {

	@Shadow
	public World world;

	@Inject(at=@At("RETURN"), method="tryHarvestBlock(Lnet/minecraft/util/math/BlockPos;)Z", cancellable=true)
	public void tryBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
		if (FabConf.isAnyEnabled("*.water_fills_on_break") && ci.getReturnValueZ()) {
			if (WaterFillsOnBreak.shouldFill(world, pos) && world.getBlockState(pos).getBlock() == Blocks.AIR) {
				world.setBlockState(pos, Blocks.WATER.getDefaultState());
			}
		}
	}


}
