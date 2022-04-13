package com.unascribed.mirage.mixin.b_utility.linkify_urls;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.Env;
import com.unascribed.mirage.util.Regex;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.regex.Matcher;

@Mixin(GuiIngame.class)
@EligibleIf(configAvailable="*.linkify_urls", envMatches=Env.CLIENT)
public class MixinInGameHud {

	@ModifyVariable(at=@At(value="HEAD"), method="addChatMessage(Lnet/minecraft/util/text/ChatType;Lnet/minecraft/util/text/ITextComponent;)V", argsOnly=true)
	public ITextComponent consume(ITextComponent message) {
		if (!FabConf.isEnabled("*.linkify_urls")) return message;
		if (!(message instanceof TextComponentTranslation && "chat.type.text".equals(((TextComponentTranslation)message).getKey()))) return message;
		Object[] args = ((TextComponentTranslation) message).getFormatArgs();
		boolean anyMatch = false;
		for (int i=0; i<args.length; i++) {
			if (args[i] instanceof String && !((String)args[i]).isEmpty()) {
				TextComponentString newLine = null;
				String astr = (String) args[i];
				Matcher matcher = Regex.webUrl.matcher(astr);
				int last = 0;
				while (matcher.find()) {
					if (newLine == null) newLine = new TextComponentString(astr.substring(last, matcher.start()));
					else newLine.appendText(astr.substring(last, matcher.start()));
					String str = matcher.group();
					TextComponentString lt = new TextComponentString(str);
					lt.setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, str)).setUnderlined(true).setItalic(true).setColor(TextFormatting.AQUA));
					newLine.appendSibling(lt);
					last = matcher.end();
				}
				if (newLine != null) {
					if (!anyMatch) anyMatch = true;
					if (last < astr.length()) newLine.appendText(astr.substring(last));
					args[i] = newLine;
				}
			}
		}
		if (anyMatch) return new TextComponentTranslation(((TextComponentTranslation) message).getKey(), args);
		return message;
	}

}
