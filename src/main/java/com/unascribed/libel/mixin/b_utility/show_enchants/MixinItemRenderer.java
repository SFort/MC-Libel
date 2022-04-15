package com.unascribed.libel.mixin.b_utility.show_enchants;

import com.google.common.collect.Lists;
import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Env;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(RenderItem.class)
@EligibleIf(anyConfigAvailable={"*.books_show_enchants", "*.tools_show_important_enchant"}, envMatches=Env.CLIENT)
public class MixinItemRenderer {

	@Inject(at=@At("TAIL"), method="renderItemOverlayIntoGUI(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
	public void renderGuiItemOverlay(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text, CallbackInfo ci) {
		if (stack == null) return;
		if (stack.getItem() == Items.ENCHANTED_BOOK && FabConf.isEnabled("*.books_show_enchants")) {
			NBTTagList tag = ItemEnchantedBook.getEnchantments(stack);
			List<Enchantment> valid = Lists.newArrayList();
			for (int i = 0; i < tag.tagCount(); i++) {
				NBTTagCompound ct = tag.getCompoundTagAt(i);
				Enchantment e = Enchantment.REGISTRY.getObjectById(ct.getShort("id"));
				if (e != null) {
					valid.add(e);
				}
			}
			if (valid.isEmpty()) return;
			int j = (int)((System.currentTimeMillis()/1000)%valid.size());
			Enchantment display = valid.get(j);
			String translated = I18n.format(display.getName());
			if (display.isCurse()) {
				String curseOfBinding = I18n.format(Enchantments.BINDING_CURSE.getName());
				String curseOfVanishing = I18n.format(Enchantments.VANISHING_CURSE.getName());
				String curseOf = StringUtils.getCommonPrefix(curseOfBinding, curseOfVanishing);
				if (!curseOf.isEmpty()) {
					if (translated.startsWith(curseOf)) {
						translated = translated.substring(curseOf.length());
					}
				}
			}
			String s = new String(Character.toChars(translated.codePoints().findFirst().getAsInt()));
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.disableBlend();
			fr.drawStringWithShadow(s, (float) (xPosition + 19 - 2 - fr.getStringWidth(s)), (float) (yPosition + 6 + 3), display.isCurse() ? 0xFFFF5555 : display.isTreasureEnchantment() ? 0xFF55FFFF : 0xFFFFFFFF);
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			GlStateManager.enableBlend();
		}
		if (FabConf.isEnabled("*.tools_show_important_enchant")) {
			Enchantment display = null;
			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
				display = Enchantments.SILK_TOUCH;
			} else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack) > 0) {
				display = Enchantments.FORTUNE;
			}
			if (display != null) {
				String translated = I18n.format(display.getName());
				String s = new String(Character.toChars(translated.codePoints().findFirst().getAsInt()));
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				GlStateManager.disableBlend();
				fr.drawStringWithShadow(s, (float) (xPosition + 19 - 2 - fr.getStringWidth(s)), (float) (yPosition + 6 + 3), 0xFFFF55FF);
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
				GlStateManager.enableBlend();
			}
		}
	}

}
