package com.unascribed.libel.mixin.i_woina.old_sheep_shear;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityAnimal.class)
@EligibleIf(configAvailable="*.old_sheep_shear")
public abstract class MixinAnimalEntity extends EntityAgeable {

	public MixinAnimalEntity(World worldIn) {
		super(worldIn);
	}

	@Inject(at=@At("HEAD"), method="attackEntityFrom")
	public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		Object self = this;
		if (!world.isRemote && self instanceof EntitySheep && FabConf.isEnabled("*.old_sheep_shear") && !((EntitySheep)self).getSheared() && source.getTrueSource() instanceof EntityPlayer) {
			((EntitySheep)self).setSheared(true);
			ItemStack wool = new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1 + this.rand.nextInt(3), ((EntitySheep)self).getFleeceColor().getMetadata());
			EntityItem itemEntity = this.entityDropItem(wool, 1);
			if (itemEntity != null) {
				itemEntity.motionY += this.rand.nextFloat() * 0.05F;
				itemEntity.motionX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
				itemEntity.motionZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
			}
		}
	}

}
