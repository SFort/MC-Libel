package com.unascribed.mirage.mixin.f_balance.disable_pearl_stasis;


import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityThrowable.class)
@EligibleIf(configAvailable="*.disable_pearl_stasis")
public abstract class MixinThrownItemEntity {
	@Inject(at=@At("TAIL"), method="writeEntityToNBT")
	public void remove_pearl_owner(NBTTagCompound tag, CallbackInfo ci) {
		if(((Object)this) instanceof EntityEnderPearl && FabConf.isEnabled("*.disable_pearl_stasis"))
			tag.removeTag("ownerName");
	}
}
