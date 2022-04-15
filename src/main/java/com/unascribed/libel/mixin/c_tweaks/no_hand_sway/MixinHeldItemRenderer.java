package com.unascribed.libel.mixin.c_tweaks.no_hand_sway;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
@EligibleIf(configAvailable="*.no_hand_sway", envMatches=Env.CLIENT)
public abstract class MixinHeldItemRenderer {
	@Shadow
	@Final
	private Minecraft mc;

	@ModifyVariable(method="rotateArm(F)V", at=@At(value = "STORE"), ordinal = 1)
	private float setH(float h) {
		return FabConf.isEnabled("*.no_hand_sway") ? this.mc.player.rotationPitch : h;
	}
	@ModifyVariable(method="rotateArm(F)V", at=@At(value = "STORE"), ordinal = 2)
	private float setV(float h) {
		return FabConf.isEnabled("*.no_hand_sway") ? this.mc.player.rotationYaw : h;
	}

}
