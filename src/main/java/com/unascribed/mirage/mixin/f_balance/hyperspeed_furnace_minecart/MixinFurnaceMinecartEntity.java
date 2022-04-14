package com.unascribed.mirage.mixin.f_balance.hyperspeed_furnace_minecart;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.interfaces.WasShoved;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityMinecartFurnace.class)
@EligibleIf(configAvailable="*.hyperspeed_furnace_minecart")
public abstract class MixinFurnaceMinecartEntity extends EntityMinecart {

	@Shadow
	private int fuel;

	public MixinFurnaceMinecartEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At("HEAD"), method="onUpdate()V")
	public void tick(CallbackInfo ci) {
		if (FabConf.isEnabled("*.hyperspeed_furnace_minecart") && !(this instanceof WasShoved && ((WasShoved)this).fabrication$wasShoved())) {
			// hyperspeed carts burn fuel 4x faster
			fuel = Math.max(0, fuel - 3);
		}
	}

	@Inject(at=@At("RETURN"), method="getMaximumSpeed()D", cancellable=true)
	public void getMaxOffRailSpeed(CallbackInfoReturnable<Double> ci) {
		if (FabConf.isEnabled("*.hyperspeed_furnace_minecart")) {
			ci.setReturnValue(Math.max(ci.getReturnValueD(), 0.6));
		}
	}

}
