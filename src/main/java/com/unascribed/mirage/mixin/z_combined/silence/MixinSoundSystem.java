package com.unascribed.mirage.mixin.z_combined.silence;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.Env;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundHandler.class)
@EligibleIf(anyConfigAvailable={"*.disable_equip_sound", "*.endermen_dont_squeal", "*.silent_minecarts"}, envMatches=Env.CLIENT)
public class MixinSoundSystem {

	@Inject(at=@At("HEAD"), method="playSound(Lnet/minecraft/client/audio/ISound;)V", cancellable=true)
	public void play(ISound sound, CallbackInfo ci) {
		ResourceLocation si = sound.getSoundLocation();
		if (si.getResourceDomain().equals("minecraft")) {
			if (FabConf.isEnabled("*.disable_equip_sound") && si.getResourcePath().equals("item.armor.equip_generic")) {
				ci.cancel();
			} else if (FabConf.isEnabled("*.endermen_dont_squeal") && (si.getResourcePath().equals("entity.enderman.scream") || si.getResourcePath().equals("entity.enderman.stare"))) {
				ci.cancel();
			} else if (FabConf.isEnabled("*.silent_minecarts") && (si.getResourcePath().equals("entity.minecart.inside") || si.getResourcePath().equals("entity.minecart.riding"))) {
				ci.cancel();
			} else if (FabConf.isEnabled("*.disable_bees") && (si.getResourcePath().startsWith("entity.bee.") || si.getResourcePath().startsWith("block.beehive."))) {
				ci.cancel();
			}
		}
	}

}
