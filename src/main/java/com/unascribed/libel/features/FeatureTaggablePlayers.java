package com.unascribed.libel.features;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.unascribed.libel.FeaturesFile;
import com.unascribed.libel.interfaces.TaggablePlayer;
import com.unascribed.libel.loaders.LoaderTaggablePlayers;
import com.unascribed.libel.support.ConfigPredicates;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Feature;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

@EligibleIf(configAvailable="*.taggable_players")
public class FeatureTaggablePlayers implements Feature {

	//TODO should probably be automatic
	public static final ImmutableSet<String> INVALID_TAGS = ImmutableSet.of(
			"weird_tweaks.extra.creepers_explode_when_on_fire"
	);

	public static final ImmutableMap<String, Integer> validTags;
	public static Map<String, Integer> activeTags = new HashMap<>();

	static {
		Map<String, Integer> tags = new HashMap<>();
		FeaturesFile.getAll().forEach((key, val)->{
			if (val.fscript == null || INVALID_TAGS.contains(key)) return;
			switch (val.fscript){
				case "PLAYER_ENTITY" :
				case "SERVER_PLAYER_ENTITY" :
					tags.put(key, 0b01);
					break;
				case "LIVING_ENTITY" :
					tags.put(key, 0b11);
					break;
			}
		});
		validTags = ImmutableMap.copyOf(tags);
	}

	private Predicate<Entity> originalUntargetablePredicate;

	private static Predicate<?> getPredicate(String key, int type) {
		switch (type) {
			case 1:
				return pe -> pe instanceof TaggablePlayer && !((TaggablePlayer) pe).fabrication$hasTag(key);
			case 2:
				return pe -> !(pe instanceof TaggablePlayer) || ((TaggablePlayer) pe).fabrication$hasTag(key);
			case 3:
				return pe -> !(pe instanceof TaggablePlayer) || !((TaggablePlayer) pe).fabrication$hasTag(key);
			default:
				return pe -> pe instanceof TaggablePlayer && ((TaggablePlayer) pe).fabrication$hasTag(key);
		}
	}

	private static void set(String key, int type){
		Predicate<?> p = getPredicate(key.substring(key.lastIndexOf('.')+1), type);
		ConfigPredicates.put(key, ConfigPredicates.defaults.containsKey(key) ? p.and((Predicate)ConfigPredicates.defaults.get(key)) : p, 1);
	}

	public static void add(String key, int type) {
		add(key, type, true);
	}

	public static void add(String key, int type, boolean save) {
		if (!validTags.containsKey(key)) return;
		type &= validTags.get(key);
		set(key, type);
		activeTags.put(key, type);
		if (save) LoaderTaggablePlayers.instance.set(key, type);
	}
	public static void remove(String key) {
		ConfigPredicates.remove(key, 1);
		activeTags.remove(key);
		LoaderTaggablePlayers.instance.remove(key);
	}

	@Override
	public void apply() {
		activeTags.forEach(FeatureTaggablePlayers::set);
	}

	@Override
	public boolean undo() {
		activeTags.keySet().forEach(k->ConfigPredicates.remove(k, 1));
		return true;
	}

	@Override
	public String getConfigKey() {
		return "*.taggable_players";
	}



}
