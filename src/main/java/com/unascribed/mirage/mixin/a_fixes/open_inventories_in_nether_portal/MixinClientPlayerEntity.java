package com.unascribed.mirage.mixin.a_fixes.open_inventories_in_nether_portal;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.Env;
import com.unascribed.mirage.support.injection.ModifyReturn;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityPlayerSP.class)
@EligibleIf(configAvailable="*.open_inventories_in_nether_portal", envMatches=Env.CLIENT)
public class MixinClientPlayerEntity {

	@ModifyReturn(method="onLivingUpdate()V", target="Lnet/minecraft/client/gui/GuiScreen;doesGuiPauseGame()Z")
	private static boolean fabrication$preventClosingScreen(boolean old) {
		return FabConf.isEnabled("*.open_inventories_in_nether_portal") || old;
	}
}
