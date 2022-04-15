package com.unascribed.mirage.mixin.z_combined.furnace_minecart;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.interfaces.WasShoved;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityMinecartFurnace.class)
@EligibleIf(anyConfigAvailable={"*.hyperspeed_furnace_minecart", "*.furnace_minecart_pushing"})
public abstract class MixinFurnaceMinecartEntity extends EntityMinecart {

	public MixinFurnaceMinecartEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At("TAIL"), method="applyDrag()V")
	public void modifyApplySlowdownVelocity(CallbackInfo ci) {
		double speed = 1;
		boolean shoved = (this instanceof WasShoved && ((WasShoved)this).fabrication$wasShoved());
		if (shoved) {
			speed = 0.2;
		} else if (FabConf.isEnabled("*.hyperspeed_furnace_minecart")) {
			speed = 2;
		}
		if (speed != 1) {
			this.motionX *= speed;
			this.motionY *= speed;
			this.motionZ *= speed;
		}
	}

}
