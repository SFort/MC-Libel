package com.unascribed.libel.mixin.e_mechanics.bottled_air;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ItemPotion.class)
@EligibleIf(configAvailable="*.bottled_air")
public class MixinPotionItem {

	@Inject(at=@At("RETURN"), method= "onItemUseFinish(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/item/ItemStack;", cancellable=true)
	public void finishUsing(ItemStack stack, World worldIn, EntityLivingBase user, CallbackInfoReturnable<ItemStack> ci) {
		if (FabConf.isEnabled("*.bottled_air") && ci.getReturnValue().getItem() == Items.GLASS_BOTTLE && user.isInWater()) {
			ci.setReturnValue(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER));
		}
	}

	@ModifyArgs(at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/InventoryPlayer;addItemStackToInventory(Lnet/minecraft/item/ItemStack;)Z"),
			method= "onItemUseFinish(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/item/ItemStack;")
	public void fabrication$bottledAir(Args args) {
		if (FabConf.isEnabled("*.bottled_air") && ((ItemStack)args.get(1)).getItem() == Items.GLASS_BOTTLE && ((InventoryPlayer)args.get(0)).player.isInWater()) {
			args.set(1, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER));
		}
	}

}
