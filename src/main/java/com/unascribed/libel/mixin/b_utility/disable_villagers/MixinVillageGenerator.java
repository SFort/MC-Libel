package com.unascribed.libel.mixin.b_utility.disable_villagers;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.world.gen.structure.MapGenVillage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MapGenVillage.class)
@EligibleIf(configAvailable="*.disable_villagers")
public class MixinVillageGenerator {
	@Inject(at=@At("HEAD"), method="canSpawnStructureAtCoords(II)Z", cancellable=true)
	public void prevent(int chunkX, int chunkZ, CallbackInfoReturnable<Boolean> cir) {
		if (FabConf.isEnabled("*.disable_villagers")) {
			cir.setReturnValue(false);
		}
	}

}
