package com.unascribed.fabrication.mixin.a_fixes.multiline_sign_paste;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sun.jna.Platform;
import com.unascribed.fabrication.support.EligibleIf;
import com.unascribed.fabrication.support.Env;

import com.google.common.base.Joiner;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

@Mixin(SignEditScreen.class)
@EligibleIf(configEnabled="*.multiline_sign_paste", envMatches=Env.CLIENT)
public abstract class MixinSignEditScreen extends Screen {

	protected MixinSignEditScreen(Text title) {
		super(title);
	}

	@Shadow
	private SelectionManager selectionManager;
	@Shadow @Final
	private String[] text;
	@Shadow @Final
	private SignBlockEntity sign;
	@Shadow
	private int currentRow;

	@Inject(at=@At("TAIL"), method="init()V")
	public void init(CallbackInfo ci) {
		this.selectionManager = new SelectionManager(() -> hasShiftDown() ? Joiner.on(Platform.isWindows() ? "\r\n" : "\n").join(text) : text[currentRow], pasted -> {
			if (pasted.contains("\n")) {
				String[] lines = pasted.split("\r?\n");
				for (int i = 0; i < lines.length; i++) {
					String line = lines[i];
					text[currentRow+i] = line;
					sign.setTextOnRow(currentRow+i, new LiteralText(line));
				}
				currentRow += lines.length-1;
			} else {
				text[currentRow] = pasted;
				sign.setTextOnRow(currentRow, new LiteralText(pasted));
			}
		}, SelectionManager.makeClipboardGetter(client),
				SelectionManager.makeClipboardSetter(client),
				pasted -> {
					if (pasted.contains("\n")) {
						String[] lines = pasted.split("\r?\n");
						if (lines.length+currentRow > text.length) return false;
						for (String line : lines) {
							if (client.textRenderer.getWidth(line) > 90) return false;
						}
						return true;
					}
					return client.textRenderer.getWidth(pasted) <= 90;
				});
	}

}