package com.unascribed.mirage.mixin.d_minor_mechanics.fire_protection_on_any_item;

import com.unascribed.mirage.FabConf;
import net.minecraft.init.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.mirage.support.EligibleIf;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

@Mixin(Enchantment.class)
@EligibleIf(configAvailable="*.fire_protection_on_any_item")
public abstract class MixinEnchantment {

	@Inject(at=@At("HEAD"), method="canApply(Lnet/minecraft/item/ItemStack;)Z", cancellable=true)
	public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
		if (FabConf.isEnabled("*.fire_protection_on_any_item") && (Object)this == Enchantments.FIRE_PROTECTION && stack.getItem().isEnchantable(stack)) {
			ci.setReturnValue(true);
		}
	}

}
