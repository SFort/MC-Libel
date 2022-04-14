package com.unascribed.mirage.mixin.d_minor_mechanics.fire_protection_on_any_item;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityItem.class)
@EligibleIf(configAvailable="*.fire_protection_on_any_item")
public abstract class MixinItemEntity {

	@Shadow
	public abstract ItemStack getItem();

	@Inject(at=@At("HEAD"), method="attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", cancellable=true)
	public void isFireImmune(DamageSource source, float f, CallbackInfoReturnable<Boolean> cir) {
		if (FabConf.isEnabled("*.fire_protection_on_any_item") && source.isFireDamage() && EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, getItem()) > 0) {
			cir.setReturnValue(false);
		}
	}

}
