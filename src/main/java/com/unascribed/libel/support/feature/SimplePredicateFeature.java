package com.unascribed.libel.support.feature;

import com.unascribed.libel.support.ConfigPredicates;
import com.unascribed.libel.support.Feature;

import java.util.function.Predicate;

public abstract class SimplePredicateFeature implements Feature {
	public final String key;
	public final Predicate<?> predicate;

	public SimplePredicateFeature(String key, Predicate<?> predicate) {
		this.key = key;
		this.predicate = predicate;
	}

	@Override
	public void apply() {
		ConfigPredicates.put(key, predicate);
	}

	@Override
	public boolean undo() {
		ConfigPredicates.remove(key);
		return true;
	}

}
