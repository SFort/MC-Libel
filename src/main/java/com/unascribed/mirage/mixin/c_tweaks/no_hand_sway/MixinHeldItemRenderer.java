package com.unascribed.mirage.mixin.c_tweaks.no_hand_sway;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.Env;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
@EligibleIf(configAvailable="*.no_hand_sway", envMatches=Env.CLIENT)
public abstract class MixinHeldItemRenderer {
	@ModifyVariable(method="rotateArm(F)V", at=@At("STORE"))
	private float setH(float h) {
		return FabConf.isEnabled("*.no_hand_sway") ? 0 : h;
	}

}
