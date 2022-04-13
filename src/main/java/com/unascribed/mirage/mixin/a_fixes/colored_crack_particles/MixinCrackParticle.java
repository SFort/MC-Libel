package com.unascribed.mirage.mixin.a_fixes.colored_crack_particles;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleBreaking;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleBreaking.class)
@EligibleIf(configAvailable="*.colored_crack_particles", envMatches=Env.CLIENT)
public abstract class MixinCrackParticle extends Particle {

	protected MixinCrackParticle(World worldIn, double posXIn, double posYIn, double posZIn) {
		super(worldIn, posXIn, posYIn, posZIn);
	}

	@Inject(at=@At("TAIL"), method="<init>(Lnet/minecraft/world/World;DDDDDDLnet/minecraft/item/Item;I)V")
	public void construct(World worldIn, double posXIn, double posYIn, double posZIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, Item itemIn, int meta, CallbackInfo ci) {
		if (!FabConf.isEnabled("*.colored_crack_particles")) return;
		int c = Minecraft.getMinecraft().getItemColors().colorMultiplier(itemIn.getDefaultInstance(), 0);
		setRBGColorF(((c>>16)&0xFF)/255f, ((c>>8)&0xFF)/255f, (c&0xFF)/255f);
	}

}
