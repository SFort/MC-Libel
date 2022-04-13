package com.unascribed.mirage.mixin.a_fixes.better_pause_freezing;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureManager.class)
@EligibleIf(configAvailable="*.better_pause_freezing", envMatches=Env.CLIENT)
public class MixinTextureManager {

	@Inject(at=@At("HEAD"), method="tick()V", cancellable=true)
	public void tick(CallbackInfo ci) {
		if (FabConf.isEnabled("*.better_pause_freezing") && Minecraft.getMinecraft().isGamePaused()) {
			ci.cancel();
		}
	}

}
