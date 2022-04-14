package com.unascribed.mirage.mixin.d_minor_mechanics.furnace_minecart_any_fuel;

import com.unascribed.mirage.FabConf;
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
@EligibleIf(configAvailable="*.furnace_minecart_any_fuel")
public abstract class MixinFurnaceMinecartEntity extends EntityMinecart {

	@Shadow
	private int fuel;

	public MixinFurnaceMinecartEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At("HEAD"), method="processInitialInteract(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Z")
	public void interact(EntityPlayer player, EnumHand hand, CallbackInfoReturnable<Boolean> cir) {
		if (!FabConf.isEnabled("*.furnace_minecart_any_fuel")) return;
		ItemStack itemStack = player.getHeldItem(hand);
		if (TileEntityFurnace.isItemFuel(itemStack)) {
			int value = TileEntityFurnace.getItemBurnTime(itemStack)*2;
			if (this.fuel + value <= 32000) {
				if (!player.capabilities.isCreativeMode) {
					itemStack.shrink(1);
				}
				this.fuel += value;
			}
		}
	}

}
