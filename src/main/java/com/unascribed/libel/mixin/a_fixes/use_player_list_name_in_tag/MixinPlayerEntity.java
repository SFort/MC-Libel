package com.unascribed.libel.mixin.a_fixes.use_player_list_name_in_tag;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
@EligibleIf(configAvailable="*.use_player_list_name_in_tag", envMatches=Env.CLIENT)
public abstract class MixinPlayerEntity extends EntityLivingBase {

	public MixinPlayerEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At("RETURN"), method="getDisplayName()Lnet/minecraft/util/text/ITextComponent;", cancellable=true)
	public void getDisplayName(CallbackInfoReturnable<ITextComponent> ci) {
		if (!FabConf.isEnabled("*.use_player_list_name_in_tag")) return;
		Object self = this;
		if (self instanceof AbstractClientPlayer) {
			ITextComponent ple = Minecraft.getMinecraft().getConnection().getPlayerInfo(this.getUniqueID()).getDisplayName();
			if (ple != null) {
				ci.setReturnValue(ple);
			}
		}
	}

}
