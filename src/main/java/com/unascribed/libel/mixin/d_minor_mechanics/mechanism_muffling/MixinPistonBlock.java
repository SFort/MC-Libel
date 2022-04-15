package com.unascribed.libel.mixin.d_minor_mechanics.mechanism_muffling;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.logic.MechanismMuffling;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.injection.Hijack;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockPistonBase.class)
@EligibleIf(configAvailable="*.mechanism_muffling")
public class MixinPistonBlock {

	@Hijack(target="Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FF)V",
			method="eventReceived(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;II)Z")
	private static boolean fabrication$muffleSound(World subject, Object player, BlockPos pos) {
		return FabConf.isEnabled("*.mechanism_muffling") && MechanismMuffling.isMuffled(subject, pos);
	}

}
