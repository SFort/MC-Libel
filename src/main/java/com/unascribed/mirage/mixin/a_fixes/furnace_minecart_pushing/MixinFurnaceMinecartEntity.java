package com.unascribed.mirage.mixin.a_fixes.furnace_minecart_pushing;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.interfaces.WasShoved;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityMinecartFurnace.class)
@EligibleIf(configAvailable="*.furnace_minecart_pushing")
public abstract class MixinFurnaceMinecartEntity extends EntityMinecart implements WasShoved {

	@Shadow
	private int fuel;

	private boolean fabrication$wasShoved;
	private boolean fabrication$wasJustShoved;

	public MixinFurnaceMinecartEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At("HEAD"), method="processInitialInteract(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Z")
	public void interactHead(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> ci) {
		if (!FabConf.isEnabled("*.furnace_minecart_pushing")) return;
		ItemStack itemStack = player.getHeldItem(hand);
		fabrication$wasJustShoved = false;
		if (!TileEntityFurnace.isItemFuel(itemStack) && this.fuel == 0) {
			this.fuel = 1;
			fabrication$wasJustShoved = true;
		}
	}

	@Inject(at=@At("RETURN"), method="processInitialInteract(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Z")
	public void interactReturn(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> ci) {
		if (!FabConf.isEnabled("*.furnace_minecart_pushing")) return;
		if (this.fuel > 0) {
			fabrication$wasShoved = fabrication$wasJustShoved;
		}
	}

	@Override
	public boolean fabrication$wasShoved() {
		return fabrication$wasShoved;
	}

}
