package com.unascribed.libel.mixin.b_utility.show_map_id;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Env;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
@EligibleIf(anyConfigAvailable={"*.show_map_id"}, envMatches=Env.CLIENT)
public class MixinItemRenderer {

	@Inject(at=@At("TAIL"), method="renderItemOverlayIntoGUI(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
	public void renderGuiItemOverlay(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text, CallbackInfo ci) {
		if (FabConf.isEnabled("*.show_map_id") && stack.getItem() == Items.FILLED_MAP) {
			String s = Integer.toString(stack.getMetadata());
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.disableBlend();
			fr.drawStringWithShadow(s, (float) (xPosition + 19 - 2 - fr.getStringWidth(s)), (float) (yPosition + 6 + 3), 16777215);
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			GlStateManager.enableBlend();
		}
	}
}
