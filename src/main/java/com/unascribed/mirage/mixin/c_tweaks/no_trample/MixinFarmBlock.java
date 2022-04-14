package com.unascribed.mirage.mixin.c_tweaks.no_trample;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.ConfigPredicates;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockFarmland.class)
@EligibleIf(anyConfigAvailable={"*.feather_falling_no_trample", "*.no_trample"})
public abstract class MixinFarmBlock extends Block{

	public MixinFarmBlock(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
	}

	@Inject(method="onFallenUpon(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V",
			at=@At("HEAD"), cancellable=true)
	public void onLandedUpon(World worldIn, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {
		if (!FabConf.isAnyEnabled("*.no_trample")) return;
		if (entity instanceof EntityLivingBase && ConfigPredicates.shouldRun("*.no_trample", (EntityLivingBase)entity)) {
			super.onFallenUpon(worldIn, pos, entity, fallDistance);
			ci.cancel();
		}
	}
}
