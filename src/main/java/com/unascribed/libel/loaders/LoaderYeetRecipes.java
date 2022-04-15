package com.unascribed.libel.loaders;

import com.google.common.collect.Sets;
import com.unascribed.libel.QDIni;
import com.unascribed.libel.support.ConfigLoader;
import net.minecraft.util.ResourceLocation;

import java.nio.file.Path;
import java.util.Set;

public class LoaderYeetRecipes implements ConfigLoader {

	public static Set<ResourceLocation> recipesToYeet = Sets.newHashSet();
	public static final LoaderYeetRecipes instance = new LoaderYeetRecipes();

	@Override
	public void load(Path configDir, QDIni config, boolean loadError) {
		Set<ResourceLocation> recipesToYeetTmp = Sets.newHashSet();
		for (String key : config.keySet()) {
			ResourceLocation id = new ResourceLocation(key);
			recipesToYeetTmp.add(id);
		}
		recipesToYeet = recipesToYeetTmp;
	}

	@Override
	public String getConfigName() {
		return "yeet_recipes";
	}

}
