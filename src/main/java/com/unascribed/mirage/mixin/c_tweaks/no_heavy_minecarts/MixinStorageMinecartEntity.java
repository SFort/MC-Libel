package com.unascribed.mirage.mixin.c_tweaks.no_heavy_minecarts;

import com.unascribed.mirage.FabConf;
import net.minecraft.entity.item.EntityMinecartContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.unascribed.mirage.support.EligibleIf;

@Mixin(EntityMinecartContainer.class)
@EligibleIf(configAvailable="*.no_heavy_minecarts")
public abstract class MixinStorageMinecartEntity {

	@ModifyVariable(method="applyDrag()V", at=@At(value="STORE", ordinal=1))
	private float undoComparatorModifier(float original) {
		if (FabConf.isEnabled("*.no_heavy_minecarts")) return 0.995f;
		return original;
	}
}
