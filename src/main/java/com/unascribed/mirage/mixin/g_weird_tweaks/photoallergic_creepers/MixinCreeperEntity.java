package com.unascribed.mirage.mixin.g_weird_tweaks.photoallergic_creepers;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityCreeper.class)
@EligibleIf(configAvailable="*.photoallergic_creepers")
public abstract class MixinCreeperEntity extends EntityMob {

	public MixinCreeperEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At("HEAD"), method="onUpdate()V")
	public void onTick(CallbackInfo ci) {
		if (FabConf.isEnabled("*.photoallergic_creepers")) {
			ItemStack helmet = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
			if (!helmet.isEmpty()) {
				if (helmet.isItemStackDamageable()) {
					helmet.setItemDamage(helmet.getItemDamage() + rand.nextInt(2));
					if (helmet.getItemDamage() >= helmet.getMaxDamage()) {
						this.renderBrokenItemStack(helmet);
						helmet.shrink(1);
					}
				}
				return;
			}
			setFire(8);
		}
	}

}
