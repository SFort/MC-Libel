package com.unascribed.libel.mixin.b_utility.item_despawn;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.unascribed.libel.support.EligibleIf;

import net.minecraft.entity.Entity;

@Mixin(WorldServer.class)
@EligibleIf(configAvailable="*.item_despawn")
public abstract class MixinServerWorld {

	@Inject(at=@At("HEAD"), method="spawnEntity(Lnet/minecraft/entity/Entity;)Z", cancellable=true)
	public void addEntity(Entity e, CallbackInfoReturnable<Boolean> ci) {
		if (e instanceof EntityItem && e.isDead) {
			// don't squawk about items set to despawn instantly
			ci.setReturnValue(false);
		}
	}


}
