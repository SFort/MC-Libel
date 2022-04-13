package com.unascribed.mirage.features;

import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.feature.EitherPredicateFeature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;

@EligibleIf(configAvailable="*.bush_walk_doesnt_hurt_with_armor")
public class FeatureBushWalkDoesntHurtWithArmor extends EitherPredicateFeature<EntityLivingBase> {

	@Override
	public String getConfigKey() {
		return "*.bush_walk_doesnt_hurt_with_armor";
	}

	public FeatureBushWalkDoesntHurtWithArmor() {
		super("*.bush_walk_doesnt_hurt",
				livingEntity -> !(livingEntity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty() || livingEntity.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty())
		);
	}
}
