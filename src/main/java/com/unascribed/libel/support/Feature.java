package com.unascribed.libel.support;

public interface Feature {
	void apply();
	boolean undo();
	String getConfigKey();
}
