package com.unascribed.libel.mixin.b_utility.taggable_players;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import com.unascribed.libel.FabConf;
import com.unascribed.libel.FabLog;
import com.unascribed.libel.features.FeatureTaggablePlayers;
import com.unascribed.libel.interfaces.TaggablePlayer;
import com.unascribed.libel.support.EligibleIf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Mixin(EntityPlayerMP.class)
@EligibleIf(configAvailable="*.taggable_players")
public abstract class MixinServerPlayerEntity extends EntityPlayer implements TaggablePlayer {

	private final Set<String> fabrication$tags = new HashSet<>();

	public MixinServerPlayerEntity(World worldIn, GameProfile gameProfileIn) {
		super(worldIn, gameProfileIn);
	}

	@Override
	public Set<String> fabrication$getTags() {
		return ImmutableSet.copyOf(fabrication$tags);
	}

	@Override
	public void fabrication$clearTags() {
		fabrication$tags.clear();
	}

	@Override
	public void fabrication$setTag(String tag, boolean enabled) {
		if (enabled) {
			fabrication$tags.add(tag);
		} else {
			fabrication$tags.remove(tag);
		}
	}

	@Override
	public boolean fabrication$hasTag(String tag) {
		return fabrication$tags.contains(tag);
	}

	@Inject(at=@At("TAIL"), method="writeEntityToNBT(Lnet/minecraft/nbt/NBTTagCompound;)V")
	public void writeCustomDataToTag(NBTTagCompound tag, CallbackInfo ci) {
		NBTTagList li = new NBTTagList();
		for (String pt : fabrication$tags) {
			li.appendTag(new NBTTagString(pt));
		}
		if (!(li.tagCount() > 0)) {
			tag.setTag("fabrication:Tags", li);
		}
	}

	@Inject(at=@At("TAIL"), method="readEntityFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)V")
	public void readCustomDataFromTag(NBTTagCompound tag, CallbackInfo ci) {
		fabrication$tags.clear();
		NBTTagList li = tag.getTagList("fabrication:Tags", new NBTTagString().getId());
		for (int i = 0; i < li.tagCount(); i++) {
			String key = li.getStringTagAt(i);
			String fullKey = FabConf.remap("*."+key.toLowerCase(Locale.ROOT));
			if (!FeatureTaggablePlayers.activeTags.containsKey(fullKey)) {
				FabLog.warn("TaggablePlayers added "+fullKey+" as a valid option because a player was tagged with it");
				FeatureTaggablePlayers.add(fullKey, 0);
			}
			fabrication$tags.add(key);
		}
	}

}
