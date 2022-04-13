package com.unascribed.mirage.mixin.b_utility.item_despawn;

import com.unascribed.mirage.interfaces.SetFromPlayerDeath;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.injection.ModifyReturn;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InventoryPlayer.class)
@EligibleIf(configAvailable="*.item_despawn")
public abstract class MixinPlayerInventory {

	@ModifyReturn(target="Lnet/minecraft/entity/player/EntityPlayer;dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/item/EntityItem;",
			method="dropAllItems()V")
	private static EntityItem fabrication$tagDroppedItem(EntityItem e) {
		if (e instanceof SetFromPlayerDeath) {
			((SetFromPlayerDeath)e).fabrication$setFromPlayerDeath(true);
		}
		return e;
	}

}
