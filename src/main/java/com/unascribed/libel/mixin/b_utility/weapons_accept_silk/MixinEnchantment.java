package com.unascribed.libel.mixin.b_utility.weapons_accept_silk;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
@EligibleIf(configAvailable="*.weapons_accept_silk")
public class MixinEnchantment {

	@Inject(at=@At("HEAD"), method="canApply(Lnet/minecraft/item/ItemStack;)Z", cancellable=true)
	public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
		if (FabConf.isEnabled("*.weapons_accept_silk") && (Object)this == Enchantments.SILK_TOUCH && EnumEnchantmentType.WEAPON.canEnchantItem(stack.getItem())) {
			ci.setReturnValue(true);
		}
	}

}
