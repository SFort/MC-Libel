package com.unascribed.libel.mixin.d_minor_mechanics.cactus_punching_hurts;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.ConfigPredicates;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInteractionManager.class)
@EligibleIf(configAvailable="*.cactus_punching_hurts")
public class MixinServerPlayerInteractionManager {


	@Shadow
	private boolean isDestroyingBlock;

	@Shadow
	public EntityPlayerMP player;

	@Shadow
	public World world;

	@Shadow
	private BlockPos destroyPos;

	@Inject(at=@At("HEAD"), method="updateBlockRemoving()V")
	private void cactusHurt(CallbackInfo ci) {
		if (FabConf.isEnabled("*.cactus_punching_hurts") && isDestroyingBlock && world.getBlockState(destroyPos).getBlock() == Blocks.CACTUS && ConfigPredicates.shouldRun("*.cactus_punching_hurts", player))
			player.attackEntityFrom(DamageSource.CACTUS, 1.0F);
	}

}
