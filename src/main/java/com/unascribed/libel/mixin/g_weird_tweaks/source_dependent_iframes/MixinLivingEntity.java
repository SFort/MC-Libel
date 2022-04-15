package com.unascribed.libel.mixin.g_weird_tweaks.source_dependent_iframes;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.interfaces.TickSourceIFrames;
import com.unascribed.libel.support.ConfigPredicates;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.LinkedHashMap;

@Mixin(EntityLivingBase.class)
@EligibleIf(configAvailable="*.source_dependent_iframes")
public abstract class MixinLivingEntity extends Entity implements TickSourceIFrames {

	private final LinkedHashMap<String, Integer> fabrication$iframeTracker = new LinkedHashMap<>();
	private int fabrication$timeUntilRegen = 0;

	public MixinLivingEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At("HEAD"), method="attackEntityFrom")
	private void checkDependentIFrames(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (!(FabConf.isEnabled("*.source_dependent_iframes") && ConfigPredicates.shouldRun("*.source_dependent_iframes", (EntityLivingBase)(Object)this))) return;
		String origin = source.getDamageType() + (source.getTrueSource() == null ? ":direct" :  source.getTrueSource().getUniqueID().toString());
		if (fabrication$iframeTracker.containsKey(origin)) {
			this.hurtResistantTime = 20;
		} else {
			fabrication$iframeTracker.put(origin, ticksExisted+9);
			this.hurtResistantTime = 0;
		}
	}
	@Inject(at=@At(value="INVOKE", target="Lnet/minecraft/entity/EntityLivingBase;damageEntity(Lnet/minecraft/util/DamageSource;F)V"), method="attackEntityFrom")
	private void setSourceDependentIFrames(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (!(FabConf.isEnabled("*.source_dependent_iframes") && ConfigPredicates.shouldRun("*.source_dependent_iframes", (EntityLivingBase)(Object)this))) return;
		if (fabrication$timeUntilRegen == 0){
			fabrication$timeUntilRegen = 10;
		}
	}
	@Inject(at=@At("HEAD"), method="onEntityUpdate()V")
	private void tickSourceDependentIFrames(CallbackInfo ci) {
		if (!FabConf.isEnabled("*.source_dependent_iframes") || ((Object)this) instanceof EntityPlayerMP) return;
		fabrication$tickSourceDependentIFrames();
	}
	@Override
	public void fabrication$tickSourceDependentIFrames(){
		if (fabrication$timeUntilRegen>0) {
			fabrication$timeUntilRegen--;
		}else if (!fabrication$iframeTracker.isEmpty()){
			Iterator<Integer> iter = fabrication$iframeTracker.values().iterator();
			iter.next();
			iter.remove();
			while (iter.hasNext()) {
				int t = iter.next()-ticksExisted;
				if (t > 0) {
					fabrication$timeUntilRegen = t;
				} else {
					iter.remove();
				}
			}
		}
	}

}
