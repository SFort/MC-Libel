package com.unascribed.mirage.features;

import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.feature.SimplePredicateFeature;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;

import java.util.function.Predicate;

@EligibleIf(configAvailable="*.feather_falling_no_trample")
public class FeatureFeatherFallingNoTrample extends SimplePredicateFeature {

	@Override
	public String getConfigKey() {
		return "*.feather_falling_no_trample";
	}

	public FeatureFeatherFallingNoTrample() {
		super("*.no_trample",
				(Predicate<EntityLivingBase>) livingEntity -> EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FEATHER_FALLING, livingEntity)>=1
		);
	}
}
