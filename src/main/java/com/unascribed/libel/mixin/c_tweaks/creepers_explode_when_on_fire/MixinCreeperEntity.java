package com.unascribed.libel.mixin.c_tweaks.creepers_explode_when_on_fire;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.ConfigPredicates;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityCreeper.class)
@EligibleIf(configAvailable="*.creepers_explode_when_on_fire")
public abstract class MixinCreeperEntity extends EntityMob {

	public MixinCreeperEntity(World worldIn) {
		super(worldIn);
	}

	@Override
	public void setFire(int ticks) {
		super.setFire(ticks);
		if (FabConf.isEnabled("*.creepers_explode_when_on_fire") && !world.isRemote &&
				ConfigPredicates.shouldRun("*.creepers_explode_when_on_fire", (EntityLivingBase)this)) {
			ignite();
		}
	}

	@Shadow
	public abstract void ignite();

}
