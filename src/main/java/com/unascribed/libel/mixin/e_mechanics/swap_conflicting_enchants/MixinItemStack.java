package com.unascribed.libel.mixin.e_mechanics.swap_conflicting_enchants;

import com.mojang.realmsclient.util.Pair;
import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(ItemStack.class)
@EligibleIf(configAvailable="*.swap_conflicting_enchants")
public abstract class MixinItemStack {

	@Shadow
	public abstract void addEnchantment(Enchantment enchantment, int level);

	@Shadow
	private NBTTagCompound capNBT;

	@Shadow
	public abstract boolean isItemEnchanted();

	@Inject(method="onItemUse(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Lnet/minecraft/util/EnumActionResult;", at=@At("HEAD"), cancellable=true)
	public void use(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ, CallbackInfoReturnable<EnumActionResult> cir) {
		if (!FabConf.isEnabled("*.swap_conflicting_enchants")) return;
		List<Pair<String, Integer>> currentConflicts = new ArrayList<>();
		NBTTagCompound lTag = capNBT.getCompoundTag("fabrication#conflictingEnchants");
		if (!lTag.hasNoTags()) {
			for (String key : lTag.getKeySet()) {
				currentConflicts.add(Pair.of(key, lTag.getInteger(key)));
			}
		}
		if (!currentConflicts.isEmpty() && player.isSneaking()) {
			NBTTagCompound tag = new NBTTagCompound();
			Pair<String, Integer> toAdd;
			{
				int rmi = world.rand.nextInt(currentConflicts.size());
				toAdd = currentConflicts.get(rmi);
				currentConflicts.remove(rmi);
			}
			Enchantment toAddEnchant = Enchantment.REGISTRY.getObject(new ResourceLocation(toAdd.first()));
			Map<Enchantment, Integer> currentEnchantments = null;
			if (this.isItemEnchanted()) {
				currentEnchantments = EnchantmentHelper.getEnchantments((ItemStack)(Object)this)
						.entrySet().stream().filter(entry -> {
							if (!entry.getKey().isCompatibleWith(toAddEnchant)) {
								tag.setInteger(String.valueOf(Enchantment.REGISTRY.getNameForObject(entry.getKey())), entry.getValue());
								return false;
							}
							return true;
						}).collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
				EnchantmentHelper.setEnchantments(currentEnchantments, (ItemStack)(Object)this);
			}
			for (Pair<String, Integer> entry : currentConflicts) {
				Enchantment enchant = Enchantment.REGISTRY.getObject(new ResourceLocation(entry.first()));
				if (currentEnchantments != null && currentEnchantments.keySet().stream().anyMatch(e->!e.isCompatibleWith(enchant))) {
					tag.setInteger(entry.first(), entry.second());
				} else {
					addEnchantment(enchant, entry.second());
				}
			}
			if (!tag.hasNoTags()) {
				capNBT.setTag("fabrication#conflictingEnchants", tag);
			}
			addEnchantment(toAddEnchant, toAdd.second());
			world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1, 1);
			cir.setReturnValue(EnumActionResult.SUCCESS);
		}
	}

}
