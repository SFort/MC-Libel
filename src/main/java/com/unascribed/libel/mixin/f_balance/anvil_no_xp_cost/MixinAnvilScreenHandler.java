package com.unascribed.libel.mixin.f_balance.anvil_no_xp_cost;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.inventory.ContainerRepair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerRepair.class)
@EligibleIf(configAvailable="*.anvil_no_xp_cost")
public abstract class MixinAnvilScreenHandler {

	@Shadow
	public int maximumCost;

	@Inject(method="updateRepairOutput()V", at=@At("TAIL"))
	public void removeCost(CallbackInfo ci) {
		if (!FabConf.isEnabled("*.anvil_no_xp_cost")) return;
		maximumCost = 0;
	}

}
