package com.unascribed.libel.mixin.a_fixes.ghast_charging;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.interfaces.GhastAttackTime;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Env;
import net.minecraft.client.renderer.entity.RenderGhast;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(RenderGhast.class)
@EligibleIf(configAvailable="*.ghast_charging", envMatches=Env.CLIENT)
public class MixinGhastEntityRenderer {
	private boolean fabrication$isShooting;
	private float fabrication$shootingTime;
	@Inject(method = "getEntityTexture(Lnet/minecraft/entity/monster/EntityGhast;)Lnet/minecraft/util/ResourceLocation;", at=@At("HEAD"))
	public void update(EntityGhast entity, CallbackInfoReturnable<ResourceLocation> cir) {
		fabrication$isShooting = entity.isAttacking();
		float a = ((((GhastAttackTime)entity).getAttackTime())+10) / 20F;
	}
	@ModifyArgs(at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/GlStateManager;scale(FFF)V"), method="preRenderCallback(Lnet/minecraft/entity/monster/EntityGhast;F)V")
	public void scale(Args args) {
		if (!FabConf.isEnabled("*.ghast_charging")) return;
		float hScale = args.get(0);
		float vScale = args.get(1);

		if (fabrication$isShooting) {
			// the old code starts 10 ticks before the "shooting" texture is assumed, but we only
			// get a packet once the texture should be assumed. the extra tiny bit of accuracy is
			// not worth needing this tweak on the server-side and sending our own packets; the
			// amount of scaling that occurs in the first 10 ticks is basically unnoticeable. so
			// add the 10 ticks we missed to the counter
			if (fabrication$shootingTime > 1) fabrication$shootingTime = 1;
			if (fabrication$shootingTime < 0) fabrication$shootingTime = 0;
			fabrication$shootingTime = 1 / (fabrication$shootingTime * fabrication$shootingTime * fabrication$shootingTime * fabrication$shootingTime * fabrication$shootingTime * 2 + 1);
			hScale = (8 + 1 / fabrication$shootingTime) / 2;
			vScale = (8 + fabrication$shootingTime) / 2;
		}
		args.set(0, hScale);
		args.set(1, vScale);
		args.set(2, hScale);
	}

}
