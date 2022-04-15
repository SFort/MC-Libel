package com.unascribed.libel.mixin.c_tweaks.long_levelup_sound_at_30;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.FabricationMod;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldClient.class)
@EligibleIf(configAvailable="*.long_levelup_sound_at_30", envMatches=Env.CLIENT)
public class MixinClientWorld {

	@Inject(at=@At("HEAD"), method="playSound(DDDLnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FFZ)V", cancellable=true)
	public void playSound(double x, double y, double z, SoundEvent event, SoundCategory category, float volume, float pitch, boolean useDistance, CallbackInfo ci) {
		if (!FabConf.isEnabled("*.long_levelup_sound_at_30")) return;
		if (event == SoundEvents.ENTITY_PLAYER_LEVELUP && category == SoundCategory.PLAYERS) {
			int lvl = Minecraft.getMinecraft().player.experienceLevel;
			if (lvl >= 25 && lvl < 30 && Minecraft.getMinecraft().player.getDistanceSq(x, y, z) < 0.05) {
				((WorldClient)(Object)this).playSound(x, y, z, FabricationMod.LEVELUP_LONG, category, pitch, volume, useDistance);
				ci.cancel();
			}
		}
	}

}
