package com.unascribed.libel.mixin.a_fixes.adventure_tags_in_survival;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
@EligibleIf(configAvailable="*.adventure_tags_in_survival")
public abstract class MixinPlayerEntity extends EntityLivingBase {

	@Shadow
	public PlayerCapabilities capabilities;

	public MixinPlayerEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At("HEAD"), method="canHarvestBlock(Lnet/minecraft/block/state/IBlockState;)Z",
			cancellable=true)
	public void isBlockBreakingRestricted(IBlockState state, CallbackInfoReturnable<Boolean> ci) {
		if (!FabConf.isEnabled("*.adventure_tags_in_survival") || capabilities.isCreativeMode || !capabilities.allowEdit) return;
		ItemStack stack = getHeldItemMainhand();
		if (!stack.isEmpty()) {
			if (stack.hasTagCompound() && stack.getTagCompound().hasKey("CanDestroy")) {
				ci.setReturnValue(!stack.canDestroy(state.getBlock()));
			}
		}
	}

	@Inject(at=@At("HEAD"), method="canPlayerEdit(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/item/ItemStack;)Z",
			cancellable=true)
	public void canPlaceOn(BlockPos pos, EnumFacing facing, ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
		// note: this isn't called for block placement for some reason; that's hardcoded into ItemStack
		// however, this *is* used for buckets, spawn eggs, etc, and may be used by other mods
		if (!FabConf.isEnabled("*.adventure_tags_in_survival") || capabilities.isCreativeMode || !capabilities.allowEdit) return;
		if (!stack.isEmpty()) {
			if (stack.hasTagCompound() && stack.getTagCompound().hasKey("CanPlaceOn")) {
				ci.setReturnValue(stack.canPlaceOn(world.getBlockState(pos.offset(facing.getOpposite())).getBlock()));
			}
		}
	}

}
