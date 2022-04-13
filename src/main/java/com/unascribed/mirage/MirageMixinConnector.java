package com.unascribed.mirage;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MirageMixinConnector implements IMixinConnector {
	@Override
	public void connect() {
		Mixins.addConfiguration("mirage.mixins.json");
	}
}
