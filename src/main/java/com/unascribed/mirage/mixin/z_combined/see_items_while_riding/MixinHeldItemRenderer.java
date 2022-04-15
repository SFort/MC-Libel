package com.unascribed.mirage.mixin.z_combined.see_items_while_riding;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.Env;
import com.unascribed.mirage.support.injection.ModifyReturn;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemRenderer.class)
@EligibleIf(anyConfigAvailable={"*.see_items_while_riding", "*.use_items_while_riding"}, envMatches=Env.CLIENT)
public class MixinHeldItemRenderer {
	@ModifyReturn(method="updateEquippedItem()V", target="Lnet/minecraft/client/entity/EntityPlayerSP;isRowingBoat()Z")
	private static boolean fabrication$heldItemView(boolean original){
		return !(FabConf.isEnabled("*.see_items_while_riding") || FabConf.isEnabled("*.use_items_while_riding")) && original;
	}
}
