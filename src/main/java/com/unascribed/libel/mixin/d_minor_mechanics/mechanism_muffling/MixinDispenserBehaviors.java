package com.unascribed.libel.mixin.d_minor_mechanics.mechanism_muffling;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.logic.MechanismMuffling;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Bootstrap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BehaviorDefaultDispenseItem.class, Bootstrap.BehaviorDispenseOptional.class, BehaviorProjectileDispense.class, Bootstrap.BehaviorDispenseBoat.class})
@EligibleIf(configAvailable="*.mechanism_muffling")
public class MixinDispenserBehaviors {

	@Inject(at=@At("HEAD"), method="playDispenseSound", cancellable=true)
	public void playSound(IBlockSource source, CallbackInfo ci) {
		if (FabConf.isEnabled("*.mechanism_muffling") && MechanismMuffling.isMuffled(source.getWorld(), source.getBlockPos())) {
			ci.cancel();
		}
	}

}
