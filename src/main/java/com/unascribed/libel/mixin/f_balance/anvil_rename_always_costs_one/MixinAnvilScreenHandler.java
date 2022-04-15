package com.unascribed.libel.mixin.f_balance.anvil_rename_always_costs_one;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerRepair.class)
@EligibleIf(configAvailable="*.anvil_rename_always_costs_one")
public abstract class MixinAnvilScreenHandler {

	@Shadow
	public int materialCost;

	@Shadow
	@Final
	private IInventory inputSlots;

	@Inject(at=@At("TAIL"), method="updateRepairOutput()V")
	public void updateResult(CallbackInfo ci) {
		if (!FabConf.isEnabled("*.anvil_rename_always_costs_one")) return;
		if (this.inputSlots.getStackInSlot(1).isEmpty()) {
			materialCost = 1;
		}
	}

}
