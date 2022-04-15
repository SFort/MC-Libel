package com.unascribed.mirage.mixin.f_balance.anvil_damage_only_on_fall;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.SpecialEligibility;
import com.unascribed.mirage.support.injection.ModifyReturn;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets="net.minecraft.inventory.ContainerRepair$2")
@EligibleIf(configAvailable="*.anvil_damage_only_on_fall")
public class MixinAnvilScreenHandler {

	@ModifyReturn(method="onTake(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", target="Ljava/util/Random;nextFloat()F")
	private static float modifyDamageChance(float chance) {
		return FabConf.isEnabled("*.anvil_damage_only_on_fall") ? 3 : chance;
	}

}
