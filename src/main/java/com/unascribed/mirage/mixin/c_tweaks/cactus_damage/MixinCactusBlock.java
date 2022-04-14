package com.unascribed.mirage.mixin.c_tweaks.cactus_damage;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.ConfigPredicates;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockCactus.class)
@EligibleIf(anyConfigAvailable={"*.cactus_walk_doesnt_hurt_with_boots", "*.cactus_brush_doesnt_hurt_with_chest"})
public class MixinCactusBlock {

	@Inject(at=@At("HEAD"), method="onEntityCollidedWithBlock(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/Entity;)V", cancellable=true)
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entity, CallbackInfo ci) {
		if (!(entity instanceof EntityLivingBase)) return;
		EntityLivingBase le = (EntityLivingBase)entity;
		boolean touchedTop = (int)(entity.posY+0.075) > pos.getY();
		if (touchedTop) {
			if (FabConf.isEnabled("*.cactus_walk_doesnt_hurt_with_boots")) {
				if (ConfigPredicates.shouldRun("*.cactus_walk_doesnt_hurt_with_boots", le)) {
					ci.cancel();
				}
			}
		} else {
			if (FabConf.isEnabled("*.cactus_brush_doesnt_hurt_with_chest")) {
				if (ConfigPredicates.shouldRun("*.cactus_brush_doesnt_hurt_with_chest", le)) {
					ci.cancel();
				}
			}
		}
	}

}
