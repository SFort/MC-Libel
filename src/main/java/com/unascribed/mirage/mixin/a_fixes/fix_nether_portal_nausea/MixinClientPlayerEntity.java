package com.unascribed.mirage.mixin.a_fixes.fix_nether_portal_nausea;

import com.mojang.authlib.GameProfile;
import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.interfaces.PortalRenderFix;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.Env;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
@EligibleIf(configAvailable="*.fix_nether_portal_nausea", envMatches=Env.CLIENT)
public abstract class MixinClientPlayerEntity extends EntityPlayer implements PortalRenderFix {

	private float fabrication$lastClientPortalTicks = 0;
	private float fabrication$nextClientPortalTicks = 0;

	public MixinClientPlayerEntity(World worldIn, GameProfile gameProfileIn) {
		super(worldIn, gameProfileIn);
	}

	@Inject(method="onUpdateWalkingPlayer()V", at=@At("HEAD"))
	private void fixPortalNausea(CallbackInfo ci){
		if (!FabConf.isEnabled("*.fix_nether_portal_nausea")) return;
		fabrication$lastClientPortalTicks = fabrication$nextClientPortalTicks;
		if (inPortal) {
			if (fabrication$nextClientPortalTicks < 1.0F){
				fabrication$nextClientPortalTicks += 0.0125F;
			}else if (fabrication$nextClientPortalTicks >= 1.0F){
				fabrication$nextClientPortalTicks = 1.0F;
			}
		}else {
			if (this.fabrication$nextClientPortalTicks > 0.0F) {
				this.fabrication$nextClientPortalTicks -= 0.05F;
			}
			if (this.fabrication$nextClientPortalTicks < 0.0F) {
				this.fabrication$nextClientPortalTicks = 0.0F;
			}
		}
	}

	public boolean fabrication$shouldRenderPortal(){
		return fabrication$nextClientPortalTicks > 0 && fabrication$lastClientPortalTicks > 0 && isPotionActive(MobEffects.NAUSEA);
	}

	public float fabrication$getPortalRenderProgress(float tickDelta){
		return fabrication$lastClientPortalTicks + (fabrication$nextClientPortalTicks - fabrication$lastClientPortalTicks) * tickDelta;
	}

}
