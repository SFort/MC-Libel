package com.unascribed.libel.mixin.i_woina.dropped_items_dont_stack;

import com.unascribed.libel.FabConf;
import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.libel.support.EligibleIf;

@Mixin(EntityItem.class)
@EligibleIf(configAvailable="*.dropped_items_dont_stack")
public abstract class MixinItemEntity {

	@Inject(at=@At("HEAD"), method="searchForOtherItemsNearby()V", cancellable=true)
	public void canMerge(CallbackInfo ci) {
		if (FabConf.isEnabled("*.dropped_items_dont_stack")) ci.cancel();
	}

	@Inject(at=@At("HEAD"), method="combineItems(Lnet/minecraft/entity/item/EntityItem;)Z", cancellable=true)
	public void canMerge(EntityItem itemstack1, CallbackInfoReturnable<Boolean> cir) {
		if (FabConf.isEnabled("*.dropped_items_dont_stack")) cir.setReturnValue(false);
	}

}
