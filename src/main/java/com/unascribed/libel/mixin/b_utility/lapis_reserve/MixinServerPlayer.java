package com.unascribed.libel.mixin.b_utility.lapis_reserve;

import com.mojang.authlib.GameProfile;
import com.unascribed.libel.interfaces.LapisReserve;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(EntityPlayerMP.class)
@EligibleIf(configAvailable="*.lapis_reserve")
public abstract class MixinServerPlayer extends EntityPlayer {
	public MixinServerPlayer(MinecraftServer p_i45285_1_, WorldServer p_i45285_2_, GameProfile p_i45285_3_, PlayerInteractionManager p_i45285_4_) {
		super(p_i45285_2_, p_i45285_3_);
	}
	@Inject(method = "copyFrom",at=@At("TAIL"))
	public void copyFrom(EntityPlayerMP oldPlayer, boolean p_copyFrom_2_, CallbackInfo ci) {
		((LapisReserve)this.inventory).setLapisreserve(((LapisReserve)oldPlayer.inventory).getLapisreserve());
	}
}
