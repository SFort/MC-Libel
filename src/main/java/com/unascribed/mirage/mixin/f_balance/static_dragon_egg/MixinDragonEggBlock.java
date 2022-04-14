package com.unascribed.mirage.mixin.f_balance.static_dragon_egg;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockDragonEgg.class)
@EligibleIf(configAvailable="*.static_dragon_egg")
public class MixinDragonEggBlock {

	@Inject(method= "teleport(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V",
			at=@At("HEAD"), cancellable=true)
	public void isPlayerInRange(World f, BlockPos f1, CallbackInfo ci) {
		if (FabConf.isEnabled("*.static_dragon_egg"))
			ci.cancel();
	}
}
