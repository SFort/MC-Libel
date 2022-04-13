package com.unascribed.mirage.mixin.b_utility.mob_ids;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
@EligibleIf(configAvailable="*.mob_ids", envMatches=Env.CLIENT)
public class MixinEntity {

	@Inject(at=@At("HEAD"), method="getCustomNameTag()Ljava/lang/String;", cancellable=true)
	public void getCustomName(CallbackInfoReturnable<String> ci) {
		Entity e = ((Entity)(Object)this);
		if (!FabConf.isEnabled("*.mob_ids") || !e.world.isRemote) return;
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.player != null && mc.player.isCreative() && mc.gameSettings.showDebugInfo) {
			ci.setReturnValue(Integer.toString(e.getEntityId()));
		}
	}

	@Inject(at=@At("HEAD"), method={"hasCustomName()Z","getAlwaysRenderNameTag()Z"}, cancellable=true)
	public void hasCustomNameAndIsVisible(CallbackInfoReturnable<Boolean> ci) {
		Entity e = ((Entity)(Object)this);
		if (!FabConf.isEnabled("*.mob_ids") || !e.world.isRemote) return;
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.player != null && mc.player.isCreative() && mc.gameSettings.showDebugInfo) {
			ci.setReturnValue(true);
		}
	}

}
