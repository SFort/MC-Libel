package com.unascribed.mirage.mixin.f_balance.disable_elytra_boost;


import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.ConfigPredicates;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFirework;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFirework.class)
@EligibleIf(configAvailable="*.disable_elytra_boost")
public abstract class MixinFireworkRocketItem {

	@Inject(at=@At(value="INVOKE", target="Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),
			method="onItemRightClick(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumHand;)Lnet/minecraft/util/ActionResult;", cancellable=true)
	private void use(World worldIn, EntityPlayer user, EnumHand hand, CallbackInfoReturnable<ActionResult<ItemStack>> cir) {
		if (FabConf.isEnabled("*.disable_elytra_boost") && ConfigPredicates.shouldRun("*.disable_elytra_boost", user)) cir.setReturnValue(ActionResult.newResult(EnumActionResult.PASS, user.getHeldItem(hand)));
	}

}
