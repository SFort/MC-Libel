package com.unascribed.mirage.support;

public interface Feature {
	void apply();
	boolean undo();
	String getConfigKey();
}
