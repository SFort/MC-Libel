package com.unascribed.libel.mixin.a_fixes.uncap_menu_fps;

import com.unascribed.libel.FabConf;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Env;

@Mixin(Minecraft.class)
@EligibleIf(configAvailable="*.uncap_menu_fps", envMatches=Env.CLIENT)
public class MixinMinecraftClient {

	@Inject(at=@At("HEAD"), method="getLimitFramerate()I", cancellable=true)
	private void getFramerateLimit(CallbackInfoReturnable<Integer> ci) {
		if (FabConf.isEnabled("*.uncap_menu_fps")) {
			ci.setReturnValue(((Minecraft)(Object)this).gameSettings.limitFramerate);
		}
	}

}
