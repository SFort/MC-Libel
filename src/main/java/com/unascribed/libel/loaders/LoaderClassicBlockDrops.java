package com.unascribed.libel.loaders;

import com.google.common.collect.Lists;
import com.unascribed.libel.QDIni;
import com.unascribed.libel.support.ConfigLoader;
import com.unascribed.libel.support.ConfigValue;
import com.unascribed.libel.support.EligibleIf;
import com.unascribed.libel.support.Env;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EligibleIf(envMatches=Env.CLIENT)
public class LoaderClassicBlockDrops implements ConfigLoader {

	public static final List<Function<ResourceLocation, ConfigValue>> rules = Lists.newArrayList();
	private static final Map<Block, Boolean> cache = new WeakHashMap<>();

	public static boolean isSafe(Block b) {
		if (cache.containsKey(b)) return cache.get(b);
		ResourceLocation id = b.getRegistryName();
		if (id == null) return false;
		for (Function<ResourceLocation, ConfigValue> rule : rules) {
			ConfigValue t = rule.apply(id);
			if (t != ConfigValue.UNSET) {
				boolean r = t.resolve(true);
				cache.put(b, r);
				return r;
			}
		}
		cache.put(b, false);
		return false;
	}

	@Override
	public void load(Path configDir, QDIni config, boolean loadError) {
		rules.clear();
		cache.clear();
		for (String k : config.keySet()) {
			if (k.startsWith("@heuristics.")) {
				if (k.contains("\\E") || k.contains("\\Q"))
					throw new IllegalArgumentException("No.");
				StringBuffer buf = new StringBuffer("^\\Q");
				Matcher m = Pattern.compile("*", Pattern.LITERAL).matcher(k.substring(12));
				while (m.find()) {
					m.appendReplacement(buf, "\\\\E.*\\\\Q");
				}
				m.appendTail(buf);
				buf.append("\\E$");
				Pattern p = Pattern.compile(buf.toString());
				Optional<Boolean> valueOpt = config.getBoolean(k);
				if (valueOpt.isPresent()) {
					boolean value = valueOpt.get();
					rules.add(id -> {
						if (p.matcher(id.getResourcePath()).matches()) return value ? ConfigValue.TRUE : ConfigValue.FALSE;
						return ConfigValue.UNSET;
					});
				}
			} else {
				Optional<Boolean> valueOpt = config.getBoolean(k);
				if (valueOpt.isPresent()) {
					boolean value = valueOpt.get();
					rules.add(id -> {
						if (id.toString().equals(k)) return value ? ConfigValue.TRUE : ConfigValue.FALSE;
						return ConfigValue.UNSET;
					});
				}
			}
		}
	}

	@Override
	public String getConfigName() {
		return "classic_block_drops";
	}

}
