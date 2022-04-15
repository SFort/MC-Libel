package com.unascribed.libel.mixin.c_tweaks.ghast_panic;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityGhast.class)
@EligibleIf(configAvailable="*.ghast_panic")
public abstract class MixinGhastEntity extends EntityFlying {

	public MixinGhastEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At("HEAD"), method="getAmbientSound", cancellable=true)
	public void getAmbientSound(CallbackInfoReturnable<SoundEvent> ci) {
		if (!FabConf.isEnabled("*.ghast_panic")) return;
		if (dimension != -1 && world.rand.nextInt(8) == 0) {
			ci.setReturnValue(SoundEvents.ENTITY_GHAST_SCREAM);
		}
	}

}
