package com.unascribed.mirage.features;

import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.Feature;
import net.minecraft.init.Blocks;

@EligibleIf(configAvailable="*.flammable_cobwebs")
public class FeatureFlammableCobwebs implements Feature {

	@Override
	public void apply() {
		Blocks.FIRE.setFireInfo(Blocks.FIRE, 60, 100);
	}

	@Override
	public boolean undo() {
		Blocks.FIRE.setFireInfo(Blocks.FIRE, 0, 0);
		return true;
	}

	@Override
	public String getConfigKey() {
		return "*.flammable_cobwebs";
	}

}
