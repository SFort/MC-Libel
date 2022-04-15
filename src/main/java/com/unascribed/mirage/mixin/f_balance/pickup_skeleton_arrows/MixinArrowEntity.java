package com.unascribed.mirage.mixin.f_balance.pickup_skeleton_arrows;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.interfaces.TagStrayArrow;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityTippedArrow.class)
@EligibleIf(configAvailable="*.pickup_skeleton_arrows")
public abstract class MixinArrowEntity implements TagStrayArrow {

	boolean fabrication$firedByStray = false;

	@Inject(at=@At("RETURN"), method="getArrowStack()Lnet/minecraft/item/ItemStack;")
	protected void asItemStack(CallbackInfoReturnable<ItemStack> cir) {
		if (!(FabConf.isEnabled("*.pickup_skeleton_arrows") && fabrication$firedByStray)) return;
		ItemStack arrow = cir.getReturnValue();
		if (!arrow.hasDisplayName())
			arrow.setStackDisplayName("Strays Arrow");
		if (!(arrow.hasTagCompound() && arrow.getTagCompound().hasKey("CustomPotionColor"))){
			if (arrow.getTagCompound() == null) arrow.setTagCompound(new NBTTagCompound());
			arrow.getTagCompound().setInteger("CustomPotionColor", PotionUtils.getPotionColor(PotionTypes.SLOWNESS));
		}
	}

	@Inject(at=@At("HEAD"), method="writeEntityToNBT")
	public void writeCustomDataToNbt(NBTTagCompound nbt, CallbackInfo ci) {
		nbt.setBoolean("fabrication$firedByStray", fabrication$firedByStray);
	}

	@Inject(at=@At("HEAD"), method="readEntityFromNBT")
	public void readCustomDataFromNbt(NBTTagCompound nbt, CallbackInfo ci) {
		fabrication$firedByStray = nbt.getBoolean("fabrication$firedByStray");
	}

	@Override
	public void fabrication$firedByStray() {
		fabrication$firedByStray = true;
	}

}
