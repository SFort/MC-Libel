package com.unascribed.mirage.mixin.d_minor_mechanics.mechanism_muffling;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.logic.MechanismMuffling;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.injection.Hijack;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDropper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({BlockDispenser.class, BlockDropper.class})
@EligibleIf(configAvailable="*.mechanism_muffling")
public class MixinDispenserBlock {

	@Hijack(target="Lnet/minecraft/world/World;playEvent(ILnet/minecraft/util/math/BlockPos;I)V",
			method="dispense(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V")
	private static boolean fabrication$preventSyncWorldEvent(World subject, int event, BlockPos pos, int i) {
		return event == 1001 && FabConf.isEnabled("*.mechanism_muffling") && MechanismMuffling.isMuffled(subject, pos);
	}

}
