package com.unascribed.mirage.mixin.b_utility.item_despawn;

import com.google.common.primitives.Ints;
import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.interfaces.SetFromPlayerDeath;
import com.unascribed.mirage.loaders.LoaderItemDespawn;
import com.unascribed.mirage.support.EligibleIf;
import com.unascribed.mirage.util.ParsedTime;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.UUID;

@Mixin(EntityItem.class)
@EligibleIf(configAvailable="*.item_despawn")
public abstract class MixinItemEntity extends Entity implements SetFromPlayerDeath {

	private long fabrication$trueAge;
	private int fabrication$extraTime;
	private boolean fabrication$invincible;
	private boolean fabrication$fromPlayerDeath;

	@Shadow
	private String thrower;

	@Shadow
	public abstract ItemStack getItem();

	@Shadow
	private int age;

	public MixinItemEntity(World worldIn) {
		super(worldIn);
	}


	@Inject(at=@At("HEAD"), method="onUpdate()V")
	public void tickHead(CallbackInfo ci) {
		if (fabrication$extraTime > 0) {
			fabrication$extraTime--;
			age--;
		}
		fabrication$trueAge++;
		if (fabrication$invincible && world instanceof WorldServer && this.posY < -32) {
			setPosition(this.posX, 1, this.posZ);
			setVelocity(0, 0, 0);
			if (!world.isRemote) {
				((WorldServer)world).getEntityTracker().sendToTracking(this, new SPacketEntityTeleport(this));
				((WorldServer)world).getEntityTracker().sendToTracking(this, new SPacketEntityVelocity(this));
			}

		}
	}

	@Inject(at=@At("HEAD"), method="attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z", cancellable=true)
	public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {
		if (fabrication$invincible || (FabConf.isEnabled("*.item_despawn") && world.isRemote)) {
			ci.setReturnValue(false);
		}
	}

	@Inject(at=@At("TAIL"), method="setItem(Lnet/minecraft/item/ItemStack;)V")
	public void setStack(ItemStack stack, CallbackInfo ci) {
		calculateDespawn();
	}

	@Inject(at=@At("TAIL"), method="setThrower(Ljava/lang/String;)V")
	public void setThrower(String thrower, CallbackInfo ci) {
		calculateDespawn();
	}

	@ModifyConstant(constant=@Constant(intValue=-32768), method="combineItems(Lnet/minecraft/entity/item/EntityItem;)Z")
	public int modifyIllegalAge(int orig) {
		// age-1 will never be equal to age; short-circuits the "age != -32768" check and allows
		// items set to "invincible" to stack together
		return fabrication$invincible ? age -1 : orig;
	}

	@Override
	public void fabrication$setFromPlayerDeath(boolean b) {
		fabrication$fromPlayerDeath = b;
		calculateDespawn();
	}

	@Unique
	private void calculateDespawn() {
		if (world.isRemote) return;
		final boolean debug = false;
		ItemStack stack = getItem();
		ParsedTime time = ParsedTime.UNSET;
		ParsedTime itemTime = LoaderItemDespawn.itemDespawns.get(stack.getItem().getRegistryName());
		if (debug) System.out.println("itemTime: "+itemTime);
		if (itemTime != null) {
			time = itemTime;
		}
		if (!time.priority) {
			if (debug) System.out.println("Not priority, check enchantments");
			for (Enchantment e : EnchantmentHelper.getEnchantments(stack).keySet()) {
				if (e.isCurse()) {
					if (LoaderItemDespawn.curseDespawn.overshadows(time)) {
						if (debug) System.out.println("Found a curse; curseDespawn overshadows: "+LoaderItemDespawn.curseDespawn);
						time = LoaderItemDespawn.curseDespawn;
					}
				} else {
					if (LoaderItemDespawn.normalEnchDespawn.overshadows(time)) {
						if (debug) System.out.println("Found an enchantment; normalEnchDespawn overshadows: "+LoaderItemDespawn.normalEnchDespawn);
						time = LoaderItemDespawn.normalEnchDespawn;
					}
					if (e.isTreasureEnchantment()) {
						if (LoaderItemDespawn.treasureDespawn.overshadows(time)) {
							if (debug) System.out.println("Found a treasure enchantment; treasureDespawn overshadows: "+LoaderItemDespawn.treasureDespawn);
							time = LoaderItemDespawn.treasureDespawn;
						}
					}
				}
				ParsedTime enchTime = LoaderItemDespawn.enchDespawns.get(e.getRegistryName());
				if (enchTime != null && enchTime.overshadows(time)) {
					if (debug) System.out.println("Found a specific enchantment; it overshadows: "+enchTime);
					time = enchTime;
				}
			}
			if (stack.hasTagCompound()) {
				if (stack.hasDisplayName() && LoaderItemDespawn.renamedDespawn.overshadows(time)) {
					if (debug) System.out.println("Item is renamed; renamedDespawn overshadows: "+LoaderItemDespawn.renamedDespawn);
					time = LoaderItemDespawn.renamedDespawn;
				}
				for (Map.Entry<String, ParsedTime> en : LoaderItemDespawn.nbtBools.entrySet()) {
					if (stack.getTagCompound().getBoolean(en.getKey())) {
						if (en.getValue().overshadows(time)) {
							if (debug) System.out.println("Found an NBT tag; it overshadows: "+en.getValue());
							time = en.getValue();
						}
					}
				}
			}
		}
		if (fabrication$fromPlayerDeath && LoaderItemDespawn.playerDeathDespawn.overshadows(time)) {
			if (debug) System.out.println("Item is from player death; playerDeathDespawn overshadows: "+LoaderItemDespawn.playerDeathDespawn);
			time = LoaderItemDespawn.playerDeathDespawn;
		}
		if (time == ParsedTime.UNSET) {
			if (debug) System.out.println("Time is unset, using default");
			time = thrower == null ? LoaderItemDespawn.dropsDespawn : LoaderItemDespawn.defaultDespawn;
		}
		if (debug) System.out.println("Final time: "+time);
		fabrication$invincible = false;
		if (time == ParsedTime.FOREVER) {
			fabrication$extraTime = 0;
			age = -32768;
		} else if (time == ParsedTime.INVINCIBLE) {
			fabrication$extraTime = 0;
			age = -32768;
			fabrication$invincible = true;
		} else if (time == ParsedTime.INSTANTLY) {
			setDead();
		} else if (time == ParsedTime.UNSET) {
			fabrication$extraTime = 0;
		} else {
			int extra = time.timeInTicks-6000;
			extra -= Ints.saturatedCast(fabrication$trueAge);
			if (extra < 0) {
				age = -extra;
				fabrication$extraTime = 0;
			} else {
				age = 0;
				fabrication$extraTime = extra;
			}
		}
	}

	@Inject(at=@At("TAIL"), method="writeEntityToNBT(Lnet/minecraft/nbt/NBTTagCompound;)V")
	public void writeCustomDataToTag(NBTTagCompound tag, CallbackInfo ci) {
		if (fabrication$extraTime > 0) tag.setInteger("fabrication:ExtraTime", fabrication$extraTime);
		tag.setLong("fabrication:TrueAge", fabrication$trueAge);
		if (fabrication$fromPlayerDeath) tag.setBoolean("fabrication:FromPlayerDeath", true);
		if (fabrication$invincible) tag.setBoolean("fabrication:Invincible", true);
	}

	@Inject(at=@At("TAIL"), method="readEntityFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V")
	public void readCustomDataFromTag(NBTTagCompound tag, CallbackInfo ci) {
		fabrication$extraTime = tag.getInteger("fabrication:ExtraTime");
		fabrication$trueAge = tag.getLong("fabrication:TrueAge");
		fabrication$fromPlayerDeath = tag.getBoolean("fabrication:FromPlayerDeath");
		fabrication$invincible = tag.getBoolean("fabrication:Invincible");
	}

}
