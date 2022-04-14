package com.unascribed.mirage.mixin.c_tweaks.ender_pearl_sound;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityEnderPearl.class)
@EligibleIf(configAvailable="*.ender_pearl_sound")
public abstract class MixinEnderPearlEntity extends EntityThrowable {

	public MixinEnderPearlEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At(value="INVOKE", target="Lnet/minecraft/entity/item/EntityEnderPearl;setDead()V"), method="onImpact")
	public void teleportSound(RayTraceResult blockpos, CallbackInfo ci) {
		if (!FabConf.isEnabled("*.ender_pearl_sound")) return;
		world.playSound(null, posX, posY, posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1, 0.5f);
	}
}
