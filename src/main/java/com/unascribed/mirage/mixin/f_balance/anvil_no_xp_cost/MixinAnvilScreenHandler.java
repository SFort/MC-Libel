package com.unascribed.mirage.mixin.f_balance.anvil_no_xp_cost;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.inventory.ContainerRepair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
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

	@ModifyConstant(method="canTakeOutput(Lnet/minecraft/entity/player/PlayerEntity;Z)Z", constant=@Constant(intValue=0))
	public int allowZero(int i) {
		if (!(FabConf.isEnabled("*.anvil_no_xp_cost") && i == 0)) return i;
		return -1;
	}

}