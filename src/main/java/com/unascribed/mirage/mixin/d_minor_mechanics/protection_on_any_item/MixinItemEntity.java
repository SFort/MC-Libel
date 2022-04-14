package com.unascribed.mirage.mixin.d_minor_mechanics.protection_on_any_item;

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
@EligibleIf(configAvailable="*.protection_on_any_item")
public abstract class MixinItemEntity {

	@Shadow
	public abstract ItemStack getItem();

	@Inject(at=@At("HEAD"), method="attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", cancellable=true)
	public void isFireImmune(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (FabConf.isEnabled("*.protection_on_any_item")) {
			int lvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, getItem());
			if (lvl>3) lvl = 4;
			switch (lvl){
				case 4: if (source.isExplosion()) cir.setReturnValue(false);
				case 3: if (source == DamageSource.LAVA) cir.setReturnValue(false);
				case 2: if (source == DamageSource.IN_FIRE || source == DamageSource.ON_FIRE) cir.setReturnValue(false);
				case 1: if (source == DamageSource.CACTUS) cir.setReturnValue(false);
			}
		}
	}

}
