package com.unascribed.mirage.mixin.z_combined.anvil_no_level_limit;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.inventory.ContainerRepair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ContainerRepair.class)
@EligibleIf(anyConfigAvailable={"*.anvil_no_level_limit", "*.anvil_no_xp_cost"})
public abstract class MixinAnvilScreenHandler {

	@ModifyConstant(method="updateRepairOutput()V", constant=@Constant(ordinal=2, intValue=40))
	public int removeLimit(int i) {
		if (!(FabConf.isEnabled("*.anvil_no_level_limit") || FabConf.isEnabled("*.anvil_no_xp_cost"))) return i;
		return Integer.MAX_VALUE;
	}

}
