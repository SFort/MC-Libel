package com.unascribed.mirage.mixin.c_tweaks.rainbow_experience;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.Env;
import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Random;

@Mixin(RenderXPOrb.class)
@EligibleIf(configAvailable="*.rainbow_experience", envMatches=Env.CLIENT)
public class MixinExperienceOrbEntityRenderer {

	private final Random fabrication$colorDecider = new Random();

	@Inject(at=@At("HEAD"), method="doRender(Lnet/minecraft/entity/item/EntityXPOrb;DDDFF)V")
	public void updateSeed(EntityXPOrb f, double f1, double f2, double f3, float f4, float f5, CallbackInfo ci) {
		if (!FabConf.isEnabled("*.rainbow_experience")) return;
		fabrication$colorDecider.setSeed(f.getUniqueID().hashCode());
	}

	@ModifyArgs(at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/BufferBuilder;color(IIII)Lnet/minecraft/client/renderer/BufferBuilder;"),
			method="doRender(Lnet/minecraft/entity/item/EntityXPOrb;DDDFF)V")
	public void addVertex(Args args) {
		if (FabConf.isEnabled("*.rainbow_experience")) {
			float hue = fabrication$colorDecider.nextFloat();
			int color1 = MathHelper.hsvToRGB(hue, 0.8f, 1);
			int color2 = MathHelper.hsvToRGB(hue+(fabrication$colorDecider.nextBoolean() ? -0.08f : 0.08f), 0.8f, 1);
			float r1 = ((color1>>16)&0xFF)/255f;
			float g1 = ((color1>>8)&0xFF)/255f;
			float b1 = ((color1>>0)&0xFF)/255f;
			float r2 = ((color2>>16)&0xFF)/255f;
			float g2 = ((color2>>8)&0xFF)/255f;
			float b2 = ((color2>>0)&0xFF)/255f;
			float a = ((int)args.get(0))/255f;
			args.set(0, (int)((r1+((r2-r1)*a))*255));
			args.set(1, (int)((g1+((g2-g1)*a))*255));
			args.set(2, (int)((b1+((b2-b1)*a))*255));
		}
	}

}
