package com.unascribed.libel.mixin.b_utility.disable_villagers;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityVillager.class)
@EligibleIf(configAvailable="*.disable_villagers")
public abstract class MixinVillagerEntity extends EntityAgeable {

	public MixinVillagerEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At("HEAD"), method="updateAITasks()V")
	public void tick(CallbackInfo ci) {
		if (FabConf.isEnabled("*.disable_villagers")) {
			this.setDead();
		}
	}

}
