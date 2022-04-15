package com.unascribed.libel.mixin.f_balance.anvil_no_xp_cost;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets="net.minecraft.inventory.ContainerRepair$2")
@EligibleIf(configAvailable="*.anvil_no_xp_cost")
public class MixinAnvilScreenHandler2 {

	@Inject(method="canTakeStack(Lnet/minecraft/entity/player/EntityPlayer;)Z", at=@At("HEAD"))
	public void modifyDamageChance(EntityPlayer p, CallbackInfoReturnable<Boolean> cir) {
		if (FabConf.isEnabled("*.anvil_no_xp_cost")) cir.setReturnValue(true);
	}

}
