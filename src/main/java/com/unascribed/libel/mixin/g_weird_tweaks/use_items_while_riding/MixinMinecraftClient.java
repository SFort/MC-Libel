package com.unascribed.libel.mixin.g_weird_tweaks.use_items_while_riding;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Env;
import com.unascribed.libel.support.injection.ModifyReturn;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Minecraft.class)
@EligibleIf(configAvailable="*.use_items_while_riding", envMatches=Env.CLIENT)
public class MixinMinecraftClient {
	@ModifyReturn(method={"rightClickMouse()V", "clickMouse()V"}, target="Lnet/minecraft/client/entity/EntityPlayerSP;isRowingBoat()Z")
	private static boolean fabrication$allowUsageWhileRiding(boolean old){
		return !FabConf.isEnabled("*.use_items_while_riding") && old;
	}
}
