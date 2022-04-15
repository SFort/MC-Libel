package com.unascribed.libel.support;

public enum Env {
	ANY,
	CLIENT,
	SERVER,
	;

	public boolean matches(boolean isClient) {
		return this == ANY || this == CLIENT && isClient || this == SERVER && !isClient;
	}
}
