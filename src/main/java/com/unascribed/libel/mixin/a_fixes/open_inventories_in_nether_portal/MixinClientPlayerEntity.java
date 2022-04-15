package com.unascribed.libel.mixin.a_fixes.open_inventories_in_nether_portal;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Env;
import com.unascribed.libel.support.injection.ModifyReturn;
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
