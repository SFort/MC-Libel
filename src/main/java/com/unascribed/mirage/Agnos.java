package com.unascribed.mirage;

import com.unascribed.mirage.support.Env;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.nio.file.Path;

// Forge implementation of Agnos. For linguistic and philosophical waffling, see the Fabric version.
public final class Agnos {

	@SideOnly(Side.CLIENT)
	public static KeyBinding registerKeyBinding(KeyBinding kb) {
		ClientRegistry.registerKeyBinding(kb);
		return kb;
	}

	public static Path getConfigDir() {
		return FabricationMod.confPath.toPath();
	}

	public static Env getCurrentEnv() {
		return FabricationMod.isClient ? Env.CLIENT : Env.SERVER;
	}

	public static boolean isModLoaded(String modid) {
		return Loader.isModLoaded(modid);
	}

	public static String getModVersion() {
		return "3.0";
	}

}
