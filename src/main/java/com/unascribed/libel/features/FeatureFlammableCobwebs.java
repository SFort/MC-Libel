package com.unascribed.libel.features;

import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Feature;
import net.minecraft.init.Blocks;

@EligibleIf(configAvailable="*.flammable_cobwebs")
public class FeatureFlammableCobwebs implements Feature {

	@Override
	public void apply() {
		Blocks.FIRE.setFireInfo(Blocks.WEB, 60, 100);
	}

	@Override
	public boolean undo() {
		Blocks.FIRE.setFireInfo(Blocks.WEB, 0, 0);
		return true;
	}

	@Override
	public String getConfigKey() {
		return "*.flammable_cobwebs";
	}

}
