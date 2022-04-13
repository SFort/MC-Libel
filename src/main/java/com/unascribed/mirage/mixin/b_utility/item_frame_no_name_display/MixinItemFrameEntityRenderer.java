package com.unascribed.mirage.mixin.b_utility.item_frame_no_name_display;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.Env;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.entity.item.EntityItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItemFrame.class)
@EligibleIf(configAvailable="*.item_frame_no_name_display", envMatches = Env.CLIENT)
public class MixinItemFrameEntityRenderer {

	@Inject(at=@At("HEAD"), method="renderName(Lnet/minecraft/entity/item/EntityItemFrame;DDD)V", cancellable=true)
	public void hasLabel(EntityItemFrame entity, double x, double y, double z, CallbackInfo ci) {
		if (FabConf.isEnabled("*.item_frame_no_name_display")) {
			ci.cancel();
		}
	}

}
