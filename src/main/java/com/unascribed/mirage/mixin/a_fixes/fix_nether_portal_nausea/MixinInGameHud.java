package com.unascribed.mirage.mixin.a_fixes.fix_nether_portal_nausea;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.interfaces.PortalRenderFix;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
@EligibleIf(configAvailable="*.fix_nether_portal_nausea", envMatches=Env.CLIENT)
public abstract class MixinInGameHud {

	@Shadow
	@Final
	protected Minecraft mc;

	@Shadow
	protected abstract void renderPortal(float timeInPortal, ScaledResolution scaledRes);

	@Inject(method="renderGameOverlay(F)V", at=@At(value="INVOKE",target="Lnet/minecraft/client/entity/EntityPlayerSP;isPotionActive(Lnet/minecraft/potion/Potion;)Z"))
	private void fixPortal(float tickDelta, CallbackInfo ci){
		if (!FabConf.isEnabled("*.fix_nether_portal_nausea")) return;
		if (((PortalRenderFix)this.mc.player).fabrication$shouldRenderPortal()) {
			this.renderPortal(((PortalRenderFix)this.mc.player).fabrication$getPortalRenderProgress(tickDelta), new ScaledResolution(this.mc));
		}
	}
}
