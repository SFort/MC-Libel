package com.unascribed.mirage.mixin.i_woina.dirt_screen;

import com.unascribed.mirage.FabConf;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.Env;

@Mixin(GuiMainMenu.class)
@EligibleIf(envMatches=Env.CLIENT, configAvailable="*.dirt_screen")
public class MixinTitleScreen extends GuiScreen {

	@Inject(method = "drawScreen(IIF)V", at = @At(value="INVOKE", target="Lnet/minecraft/client/gui/GuiMainMenu;renderSkybox(IIF)V", shift = At.Shift.AFTER))
	public void drawDirt(CallbackInfo ci) {
		if (FabConf.isEnabled("*.dirt_screen")) {
			drawBackground(0);
		}
	}
}
