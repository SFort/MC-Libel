package com.unascribed.mirage.mixin.b_utility.disable_villagers;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityZombieVillager.class)
@EligibleIf(configAvailable="*.disable_villagers")
public abstract class MixinZombieVillagerEntity extends EntityZombie {

	public MixinZombieVillagerEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At("HEAD"), method="onUpdate()V")
	public void tick(CallbackInfo ci) {
		if (FabConf.isEnabled("*.disable_villagers")) {
			this.setDead();
		}
	}

}
