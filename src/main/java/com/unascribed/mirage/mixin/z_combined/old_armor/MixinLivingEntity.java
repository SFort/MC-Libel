package com.unascribed.mirage.mixin.z_combined.old_armor;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.ConfigPredicates;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.support.injection.ModifyReturn;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.AbstractMap;
import java.util.stream.Collector;

@Mixin(EntityLivingBase.class)
@EligibleIf(anyConfigAvailable={"*.old_armor_scale", "old_armor"})
public abstract class MixinLivingEntity extends Entity {

	public MixinLivingEntity(World worldIn) {
		super(worldIn);
	}

	@Shadow
	public abstract ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn);

	@Shadow
	public abstract AbstractAttributeMap getAttributeMap();

	@Inject(at=@At("HEAD"), method="applyArmorCalculations(Lnet/minecraft/util/DamageSource;F)F")
	public void refreshArmor(DamageSource source, float amount, CallbackInfoReturnable<Float> cir){
		EntityEquipmentSlot[] slots = new EntityEquipmentSlot[]{
				EntityEquipmentSlot.FEET,
				EntityEquipmentSlot.LEGS,
				EntityEquipmentSlot.CHEST,
				EntityEquipmentSlot.HEAD
		};
		for(EntityEquipmentSlot slot : slots) {
			ItemStack itemStack3 = this.getItemStackFromSlot(slot);
				if (!itemStack3.isEmpty()) {
					this.getAttributeMap().removeAttributeModifiers(fabrication$oldArmor(itemStack3.getAttributeModifiers(slot), itemStack3, slot, (EntityLivingBase)(Object)this));
					this.getAttributeMap().removeAttributeModifiers(fabrication$oldArmor(itemStack3.getAttributeModifiers(slot), itemStack3, slot, (EntityLivingBase)(Object)this));
				}
		}
	}

	//Purely for visuals
	@Inject(at=@At("HEAD"), method="onEntityUpdate()V")
	public void tickArmor(CallbackInfo ci){
		if ((this.ticksExisted & 16) == 0) refreshArmor(null, 0, null);
	}

	@ModifyReturn(target="Lnet/minecraft/item/ItemStack;getAttributeModifiers(Lnet/minecraft/inventory/EntityEquipmentSlot;)Lcom/google/common/collect/Multimap;",
			method="onUpdate()V")
	private static Multimap<String, AttributeModifier> fabrication$oldArmor(Multimap<String, AttributeModifier> map, ItemStack stack, EntityEquipmentSlot slot, EntityLivingBase self) {
		final boolean scale = FabConf.isEnabled("*.old_armor_scale") && ConfigPredicates.shouldRun("*.old_armor_scale", self);
		final boolean old = FabConf.isEnabled("*.old_armor") && ConfigPredicates.shouldRun("*.old_armor", self);
		if (!(((scale && stack.isItemStackDamageable()) || old) && stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).getEquipmentSlot() == slot)) return map;
		return map.entries().stream().map(
				entry ->
					("Armor modifier".equals(entry.getKey()) && entry.getValue().getOperation() == 0 ?
							new AbstractMap.SimpleEntry<>(
									entry.getKey(),
									new AttributeModifier(
											entry.getValue().getID(),
											entry.getValue().getName(),
											(old ? ItemArmor.ArmorMaterial.DIAMOND.getDamageReductionAmount(slot) : entry.getValue().getAmount())
													* (scale ? ((stack.getMaxDamage() - stack.getItemDamage()) / (double) stack.getMaxDamage()) : 1),
											0))
							: new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()))
				).collect(Collector.of(ArrayListMultimap::create, (m, entry) ->m.put(entry.getKey(), entry.getValue()), (m1, m2) -> {m1.putAll(m2); return m1;}));

	}
}
