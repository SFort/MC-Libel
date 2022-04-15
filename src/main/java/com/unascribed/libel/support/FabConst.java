package com.unascribed.libel.support;

import com.unascribed.libel.FabLog;

public class FabConst {
	public static final boolean DEV;
	public static final boolean FORGE;
	static {
		boolean devTmp;
		boolean forgeTmp;
		try {
			Class.forName("net.minecraft.util.ResourceLocation", false, FabConst.class.getClassLoader());
			devTmp = true;
			forgeTmp = false;
		} catch (ClassNotFoundException e) {
			devTmp = false;
			try {
				Class.forName("net.minecraft.util.ResourceLocation", false, FabConst.class.getClassLoader());
				forgeTmp = true;
			} catch (ClassNotFoundException e2) {
				forgeTmp = false;
			}
		}
		DEV = devTmp;
		FORGE = forgeTmp;
		FabLog.debug("Detected runtime: "+(DEV ? "Fabric Dev" : FORGE ? "Forge" : "Fabric"));

	}
}
