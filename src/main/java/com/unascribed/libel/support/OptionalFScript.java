package com.unascribed.libel.support;

import com.unascribed.libel.FabConf;
import com.unascribed.libel.FabLog;
import com.unascribed.libel.FeaturesFile;
import com.unascribed.libel.loaders.LoaderFScript;
import tf.ssf.sfort.script.Default;
import tf.ssf.sfort.script.PredicateProvider;
import tf.ssf.sfort.script.ScriptParser;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OptionalFScript {
	private static final Map<String, PredicateProvider<?>> predicateProviders;
	static {
		predicateProviders = FeaturesFile.getAll().entrySet().stream().filter(f -> f.getValue().fscript != null && Default.getDefaultMap().containsKey(f.getValue().fscript)).collect(Collectors.toMap(Map.Entry::getKey, v -> Default.getDefaultMap().get(v.getValue().fscript)));
	}

	public static void set(String configKey, String script, Consumer<Exception> errReporter){
		setScript(configKey, script).ifPresent(errReporter);
	}

	public static void reload(){
		for (Map.Entry<String, String> entry : LoaderFScript.getMap().entrySet()){
			setScript(entry.getKey(), entry.getValue());
		}
	}
	private static Optional<Exception> setScript(String configKey, String script){
		configKey = FabConf.remap(configKey);
		try {
			PredicateProvider<?> predicateProvider = predicateProviders.get(configKey);
			if (predicateProvider == null) return Optional.of(new Exception("No predicate provider exists for specified key"));
			Predicate<?> predicate = new ScriptParser<>(predicateProvider).parse(script);
			if (predicate == null ) return Optional.of(new Exception("FScript returned null, likely because an invalid script was given"));
			ConfigPredicates.put(configKey, predicate, 2);
			LoaderFScript.put(configKey, script);
		}catch (Exception e){
			FabLog.error("Failed to set script for "+configKey, e);
			return Optional.of(e);
		}
		return Optional.empty();
	}

}
