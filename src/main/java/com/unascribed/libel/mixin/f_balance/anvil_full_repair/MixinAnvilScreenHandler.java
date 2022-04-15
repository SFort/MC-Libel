package com.unascribed.libel.mixin.f_balance.anvil_full_repair;

import com.unascribed.libel.FabConf;
import net.minecraft.inventory.ContainerRepair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.unascribed.libel.support.EligibleIf;

@Mixin(ContainerRepair.class)
@EligibleIf(configAvailable="*.anvil_full_repair")
public abstract class MixinAnvilScreenHandler {

	@ModifyArg(method = "updateRepairOutput()V", at=@At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setItemDamage(I)V"))
	public int fullrepair(int i) {
		if (FabConf.isEnabled("*.anvil_full_repair"))
			return 0;
		return i;
	}
}
