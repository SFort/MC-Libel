package com.unascribed.mirage.mixin.b_utility.killmessage;

import com.unascribed.mirage.FabConf;
import com.unascribed.mirage.interfaces.GetKillMessage;
import com.unascribed.mirage.support.EligibleIf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin({EntityDamageSource.class, EntityDamageSourceIndirect.class})
@EligibleIf(configAvailable="*.killmessage")
public abstract class MixinEntityDamageSource {

	@Unique
	private static final Pattern fabrication$placeholderPattern = Pattern.compile("(?<!%)%(?:([123])\\$)?s");

	@Inject(at=@At("HEAD"), method="getDeathMessage(Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/util/text/ITextComponent;", cancellable=true)
	public void getDeathMessage(EntityLivingBase victim, CallbackInfoReturnable<ITextComponent> cir) {
		if (!FabConf.isEnabled("*.killmessage")) return;
		Entity attacker = ((EntityDamageSource)(Object)this).getTrueSource();
		if (attacker instanceof GetKillMessage) {
			Iterator<ItemStack> iter = attacker.getHeldEquipment().iterator();
			ItemStack held;
			if (iter.hasNext()) {
				held = iter.next();
			} else {
				held = ItemStack.EMPTY;
			}
			String msg = null;
			if (held.hasTagCompound() && held.getTagCompound().hasKey("KillMessage", new NBTTagString().getId())) {
				msg = held.getTagCompound().getString("KillMessage");
			}
			if (msg == null) {
				msg = ((GetKillMessage)attacker).fabrication$getKillMessage();
			}
			if (msg != null) {
				Matcher m = fabrication$placeholderPattern.matcher(msg);
				if (m.find()) {
					m.reset(msg);
					StringBuilder base = new StringBuilder();

					int prev = 0;
					int defIdx = 0;
					while (m.find()) {
						if (prev < msg.length()) {
							base.append(msg, prev, m.start());
						}
						int idx = defIdx;
						if (m.group(1) != null) {
							idx = Integer.parseInt(m.group(1))-1;
						} else {
							defIdx++;
						}
						if (idx == 0) {
							base.append(victim.getDisplayName());
						} else if (idx == 1) {
							base.append(attacker.getDisplayName());
						} else if (idx == 2) {
							base.append(held.getTextComponent());
						} else {
							base.append(m.group());
						}
						prev = m.end();
					}
					if (prev < msg.length()) {
						base.append(msg.substring(prev));
					}
					cir.setReturnValue(new TextComponentString(base.toString()));
				} else {
					cir.setReturnValue(new TextComponentString(msg));
				}
			}
		}
	}

}
