package com.unascribed.mirage.features;

import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.feature.EitherPredicateFeature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

@EligibleIf(configAvailable="*.bush_walk_doesnt_hurt_when_sneaking")
public class FeatureBushWalkDoesntHurtWhenSneaking extends EitherPredicateFeature<EntityLivingBase> {

	@Override
	public String getConfigKey() {
		return "*.bush_walk_doesnt_hurt_when_sneaking";
	}

	public FeatureBushWalkDoesntHurtWhenSneaking() {
		super("*.bush_walk_doesnt_hurt", Entity::isSneaking);
	}
}
