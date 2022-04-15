package com.unascribed.libel.mixin.i_woina.old_background_shade;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Env;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(GuiScreen.class)
@EligibleIf(configAvailable="*.old_background_shade", envMatches=Env.CLIENT)
public class MixinScreen {

	@ModifyConstant(constant=@Constant(intValue=-1072689136), method="drawWorldBackground(I)V")
	public int modifyTopBgColor(int color) {
		if (!FabConf.isEnabled("*.old_background_shade")) return color;
		return 0x60050500;
	}

	@ModifyConstant(constant=@Constant(intValue=-804253680), method="drawWorldBackground(I)V")
	public int modifyBottomBgColor(int color) {
		if (!FabConf.isEnabled("*.old_background_shade")) return color;
		return 0xA0303060;
	}
}
