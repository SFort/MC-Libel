package com.unascribed.libel.mixin.g_weird_tweaks.disable_equip_sound;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
@EligibleIf(configAvailable="*.disable_equip_sound")
public class MixinEntity {

	@Inject(at=@At("HEAD"), method="playSound", cancellable=true)
	public void playSound(SoundEvent soundIn, float volume, float pitch, CallbackInfo ci) {
		if (!FabConf.isEnabled("*.disable_equip_sound")) return;
		if (soundIn == SoundEvents.ITEM_ARMOR_EQUIP_GENERIC) {
			ci.cancel();
		}
	}

}
