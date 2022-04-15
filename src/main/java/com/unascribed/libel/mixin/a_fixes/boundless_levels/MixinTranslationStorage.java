package com.unascribed.libel.mixin.a_fixes.boundless_levels;

import com.unascribed.libel.FabConf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.text.translation.I18n;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Env;
import com.unascribed.libel.util.RomanNumeral;

import com.google.common.primitives.Ints;

@Mixin(I18n.class)
@EligibleIf(configAvailable="*.boundless_levels", envMatches=Env.CLIENT)
public class MixinTranslationStorage {

	@Inject(at=@At("HEAD"), method="translateToLocal(Ljava/lang/String;)Ljava/lang/String;", cancellable=true)
	private static void get(String key, CallbackInfoReturnable<String> ci) {
		if (!FabConf.isEnabled("*.boundless_levels")) return;
		if (key.startsWith("enchantment.level.")) {
			Integer i = Ints.tryParse(key.substring(18));
			if (i != null) {
				ci.setReturnValue(RomanNumeral.format(i));
			}
		}
		if (key.startsWith("potion.potency.")) {
			Integer i = Ints.tryParse(key.substring(15));
			if (i != null) {
				if (i == 0) {
					ci.setReturnValue("");
				} else {
					ci.setReturnValue(RomanNumeral.format(i+1));
				}
			}
		}
	}

}
