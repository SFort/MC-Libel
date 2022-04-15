package com.unascribed.libel.mixin.i_woina.instant_bow;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.ConfigPredicates;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBow.class)
@EligibleIf(configAvailable="*.instant_bow")
public abstract class MixinBowItem {

	@Shadow
	public abstract void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft);

	@Inject(method="onItemRightClick(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/ActionResult;",
			at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/EntityPlayer;setActiveHand(Lnet/minecraft/util/EnumHand;)V", shift=At.Shift.BEFORE), cancellable=true)
	private void getUseAction(World world, EntityPlayer user, EnumHand hand, CallbackInfoReturnable<ActionResult<ItemStack>> cir){
		if(!(FabConf.isEnabled("*.instant_bow") && ConfigPredicates.shouldRun("*.instant_bow", user))) return;
		ItemStack itemStack = user.getHeldItem(hand);
		onPlayerStoppedUsing(itemStack, world, user, 0);
		cir.setReturnValue(new ActionResult<>(EnumActionResult.FAIL, itemStack));
	}
}
