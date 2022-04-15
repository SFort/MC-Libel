package com.unascribed.libel.mixin.d_minor_mechanics.unsaddle_creatures;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPig.class)
@EligibleIf(configAvailable="*.unsaddle_creatures")
public abstract class MixinPigEntity {

	@Shadow
	public abstract boolean getSaddled();

	@Shadow
	public abstract void setSaddled(boolean saddled);

	@Inject(method="processInteract(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Z",
			at=@At("HEAD"), cancellable=true)
	public void interactMob(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> cir) {
		if (FabConf.isEnabled("*.unsaddle_creatures") && this.getSaddled() && player.isSneaking() && player.getHeldItem(hand).isEmpty()) {
			setSaddled(false);
			player.setHeldItem(hand, Items.SADDLE.getDefaultInstance());
			cir.setReturnValue(true);
		}
	}
}
