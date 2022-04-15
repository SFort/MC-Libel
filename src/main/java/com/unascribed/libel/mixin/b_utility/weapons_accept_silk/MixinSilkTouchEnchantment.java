package com.unascribed.libel.mixin.b_utility.weapons_accept_silk;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentUntouching;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentUntouching.class)
@EligibleIf(configAvailable="*.weapons_accept_silk")
public class MixinSilkTouchEnchantment extends Enchantment {

	protected MixinSilkTouchEnchantment(Rarity rarityIn, EnumEnchantmentType typeIn, EntityEquipmentSlot[] slots) {
		super(rarityIn, typeIn, slots);
	}

	@Inject(at=@At("HEAD"), method="canApplyTogether(Lnet/minecraft/enchantment/Enchantment;)Z", cancellable=true)
	public void canAccept(Enchantment other, CallbackInfoReturnable<Boolean> ci) {
		if (!FabConf.isEnabled("*.weapons_accept_silk")) return;
		if (other == Enchantments.LOOTING) {
			ci.setReturnValue(false);
		}
	}

}
