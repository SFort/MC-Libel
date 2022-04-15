package com.unascribed.libel.mixin.i_woina.full_enchanting_cost;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerEnchantment.class)
@EligibleIf(configAvailable="*.full_enchanting_cost")
public class MixinEnchantmentScreenHandler {

	@Shadow
	public int[] enchantLevels;

	@Shadow
	public IInventory tableInventory;

	@Inject(method="enchantItem(Lnet/minecraft/entity/player/EntityPlayer;I)Z",
			at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/EntityPlayer;onEnchant(Lnet/minecraft/item/ItemStack;I)V"))
	private void fullExperienceCost(EntityPlayer playerEntity, int i, CallbackInfoReturnable<Boolean> cir) {
		if (FabConf.isEnabled("*.full_enchanting_cost"))
			playerEntity.onEnchant(this.tableInventory.getStackInSlot(0), this.enchantLevels[i] -1 -i);
	}

}
