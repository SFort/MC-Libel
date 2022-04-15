package com.unascribed.libel.mixin.b_utility.killmessage;

import com.unascribed.libel.interfaces.GetKillMessage;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
@EligibleIf(configAvailable="*.killmessage")
public abstract class MixinEntity implements GetKillMessage {

	private String fabrication$killmessage = null;

	@Override
	public String fabrication$getKillMessage() {
		return fabrication$killmessage;
	}

	@Inject(at=@At("TAIL"), method="writeToNBT(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/nbt/NBTTagCompound;")
	public void toTag(NBTTagCompound tag, CallbackInfoReturnable<NBTTagCompound> ci) {
		if (fabrication$killmessage != null) {
			tag.setString("KillMessage", fabrication$killmessage);
		}
	}

	@Inject(at=@At("TAIL"), method="readFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V")
	public void fromTag(NBTTagCompound tag, CallbackInfo ci) {
		if (tag.hasKey("KillMessage", new NBTTagString().getId()) ) {
			fabrication$killmessage = tag.getString("KillMessage");
		}
	}

}
