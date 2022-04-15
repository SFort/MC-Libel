package com.unascribed.libel.mixin.a_fixes.multiline_sign_paste;

import com.google.common.base.Joiner;
import com.sun.jna.Platform;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Env;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.event.KeyEvent;

@Mixin(GuiEditSign.class)
@EligibleIf(configAvailable="*.multiline_sign_paste", envMatches=Env.CLIENT)
public abstract class MixinSignEditScreen extends GuiScreen {

	@Shadow
	@Final
	private TileEntitySign tileSign;

	@Shadow
	private int editLine;

	@Inject(at=@At("TAIL"), method="initGui()V")
	public void init(CallbackInfo ci) {
		String pasted = getClipboardString();
		if (pasted.contains("\n")) {
			String[] lines = pasted.split("\r?\n");
			for (String line : lines) {
				tileSign.signText[editLine] = new TextComponentString(line);
			}
			editLine += lines.length-1;
		} else {
			tileSign.signText[editLine] = new TextComponentString(pasted);
		}
	}

	@Inject(at=@At("HEAD"), method= "keyTyped(CI)V", cancellable=true)
	public void keyPressed(char chr, int keyCode, CallbackInfo ci) {
		if (keyCode == KeyEvent.VK_C && isCtrlKeyDown() && isShiftKeyDown() && !isAltKeyDown()) {
			setClipboardString(Joiner.on(Platform.isWindows() ? "\r\n" : "\n").join(tileSign.signText));
			ci.cancel();
		}
	}

}
