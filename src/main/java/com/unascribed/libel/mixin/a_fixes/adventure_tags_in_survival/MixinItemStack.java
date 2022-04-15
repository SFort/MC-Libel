package com.unascribed.libel.mixin.a_fixes.adventure_tags_in_survival;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
@EligibleIf(configAvailable="*.adventure_tags_in_survival")
public class MixinItemStack {

	@Inject(at=@At("HEAD"), method="onItemUse(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Lnet/minecraft/util/EnumActionResult;",
			cancellable=true)
	public void useOnBlock(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ, CallbackInfoReturnable<EnumActionResult> ci) {
		if (!FabConf.isEnabled("*.adventure_tags_in_survival")) return;
		if (player != null && (player.capabilities.isCreativeMode || player.capabilities.allowEdit)) return;
		ItemStack self = (ItemStack)(Object)this;
		if (!self.isEmpty()) {
			if (self.hasTagCompound() && self.getTagCompound().hasKey("CanPlaceOn")) {
				boolean able = self.canPlaceOn(world.getBlockState(pos.offset(side.getOpposite())).getBlock());
				if (!able) {
					ci.setReturnValue(EnumActionResult.PASS);
				}
			}
		}
	}

}
