package com.unascribed.libel.mixin.e_mechanics.swap_conflicting_enchants;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.injection.ModifyReturn;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ContainerRepair.class)
@EligibleIf(configAvailable="*.swap_conflicting_enchants")
public abstract class MixinAnvilScreenHandler extends Container {

	@Shadow
	@Final
	private IInventory outputSlot;

	@ModifyReturn(method="updateRepairOutput()V", target="Lnet/minecraft/enchantment/Enchantment;isCompatibleWith(Lnet/minecraft/enchantment/Enchantment;)Z")
	private static boolean fabrication$allowConflictingEnchants(boolean old, Enchantment e1, Enchantment e2) {
		return old || FabConf.isEnabled("*.swap_conflicting_enchants") && e1 != e2;
	}

	@ModifyReturn(method="updateRepairOutput()V", target="Lnet/minecraft/enchantment/EnchantmentHelper;getEnchantments(Lnet/minecraft/item/ItemStack;)Ljava/util/Map;")
	private static Map<Enchantment, Integer> fabrication$loadConflictingEnchants(Map<Enchantment, Integer> old, ItemStack stack) {
		if (FabConf.isEnabled("*.swap_conflicting_enchants")) {
			NBTTagCompound tag = stack.getSubCompound("fabrication#conflictingEnchants");
			if (tag != null && !tag.hasNoTags()) {
				for (String key : tag.getKeySet()) {
					old.put(Enchantment.REGISTRY.getObject(new ResourceLocation(key)), tag.getInteger(key));
				}
			}
		}
		return old;
	}

	@Inject(at=@At("TAIL"), method="updateRepairOutput()V")
	public void allowCombiningIncompatibleEnchants(CallbackInfo ci) {
		if (!FabConf.isEnabled("*.swap_conflicting_enchants")) return;
		ItemStack stack = outputSlot.getStackInSlot(0);
		if (stack.isItemEnchanted()) {
			NBTTagCompound conflictingEnchants = new NBTTagCompound();
			Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
			Enchantment[] enchantList = enchants.keySet().toArray(new Enchantment[0]);
			for (int i=0; i<enchantList.length; i++) {
				for (int ii=i+1; ii<enchantList.length; ii++){
					if (!enchantList[i].isCompatibleWith(enchantList[ii])) {
						conflictingEnchants.setInteger(String.valueOf(Enchantment.REGISTRY.getNameForObject(enchantList[i])), enchants.get(enchantList[i]));
						enchants.remove(enchantList[i]);
						break;
					}
				}
			}
			if (!conflictingEnchants.hasNoTags()) {
				if (stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());
				EnchantmentHelper.setEnchantments(enchants, stack);
				stack.getTagCompound().setTag("fabrication#conflictingEnchants", conflictingEnchants);
			}
		}
	}


}
