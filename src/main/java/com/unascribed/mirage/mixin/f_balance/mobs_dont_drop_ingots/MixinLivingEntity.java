package com.unascribed.mirage.mixin.f_balance.mobs_dont_drop_ingots;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityLiving.class)
@EligibleIf(configAvailable="*.mobs_dont_drop_ingots")
public class MixinLivingEntity {

	@ModifyArg(method="dropLoot(ZILnet/minecraft/util/DamageSource;)V", at=@At(value="INVOKE", target="Lnet/minecraft/entity/EntityLiving;entityDropItem(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/item/EntityItem;"))
	public ItemStack generateLoot(ItemStack stack) {
		if(!FabConf.isEnabled("*.mobs_dont_drop_ingots")) return stack;
			Item replacement = null;
			Item current = stack.getItem();
			if (Items.IRON_INGOT.equals(current)) replacement = Items.IRON_NUGGET;
			else if (Items.GOLD_INGOT.equals(current)) replacement = Items.GOLD_NUGGET;
			if (replacement != null) {
				NBTTagCompound tag = new NBTTagCompound();
				stack.writeToNBT(tag);
				tag.setString("id", Item.REGISTRY.getNameForObject(replacement).toString());
				return new ItemStack(tag);
			}
			return ItemStack.EMPTY;
	}
}
