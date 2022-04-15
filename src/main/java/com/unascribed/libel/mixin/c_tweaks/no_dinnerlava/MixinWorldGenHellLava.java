package com.unascribed.libel.mixin.c_tweaks.no_dinnerlava;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenHellLava;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(WorldGenHellLava.class)
@EligibleIf(configAvailable="*.no_dinnerlava")
public class MixinWorldGenHellLava {
	@Inject(method="generate(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;)Z", at=@At("HEAD"), cancellable=true)
	public void noDinnerLava(World worldIn, Random rand, BlockPos position, CallbackInfoReturnable<Boolean> cir){
		if (FabConf.isEnabled("*.no_dinnerlava")) cir.setReturnValue(false);
	}
}
