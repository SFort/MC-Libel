package com.unascribed.libel.mixin.d_minor_mechanics.protection_on_any_item;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
@EligibleIf(configAvailable="*.protection_on_any_item")
public abstract class MixinEnchantment {

	@Inject(at=@At("HEAD"), method="canApply(Lnet/minecraft/item/ItemStack;)Z", cancellable=true)
	public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
		if (FabConf.isEnabled("*.protection_on_any_item") && (Object)this == Enchantments.PROTECTION && stack.getItem().isEnchantable(stack)) {
			ci.setReturnValue(true);
		}
	}

}
