package com.unascribed.libel.mixin.b_utility.lapis_reserve;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.interfaces.LapisReserve;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerEnchantment.class)
@EligibleIf(configAvailable="*.lapis_reserve")
public abstract class MixinEnchantScreen extends Container {
	@Shadow
	public IInventory tableInventory;

	@Inject(method="<init>(Lnet/minecraft/entity/player/InventoryPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V", at=@At("RETURN"))
	public void open(InventoryPlayer p_i45798_1_, World p_i45798_2_, BlockPos p_i45798_3_, CallbackInfo ci) {
		if (!FabConf.isEnabled("*.lapis_reserve")) return;
		tableInventory.setInventorySlotContents(1, ((LapisReserve)p_i45798_1_).getLapisreserve());
	}
	@Inject(method= "onContainerClosed(Lnet/minecraft/entity/player/EntityPlayer;)V", at=@At("HEAD"))
	public void close(EntityPlayer player, CallbackInfo info) {
		if (!FabConf.isEnabled("*.lapis_reserve")) return;
		((LapisReserve)player.inventory).setLapisreserve(tableInventory.getStackInSlot(1));
		tableInventory.setInventorySlotContents(1, ItemStack.EMPTY);
	}
}
